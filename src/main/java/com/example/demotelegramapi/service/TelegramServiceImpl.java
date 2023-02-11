package com.example.demotelegramapi.service;

import com.example.demotelegramapi.configuration.ConfigurationData;
import com.example.demotelegramapi.helpers.SimpleHelper;
import com.example.demotelegramapi.handlers.*;
import com.example.demotelegramapi.model.TelegramContent;
import org.drinkless.tdlib.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOError;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.LongStream;

@Service
public class TelegramServiceImpl implements TelegramService {
    private static Client client = null;
    private TdApi.AuthorizationState authorizationState = null;
    private volatile boolean needQuit = false;
    private final Lock authorizationLock = new ReentrantLock();
    private final Condition gotAuthorization = authorizationLock.newCondition();
    private final AtomicBoolean replyReceived = new AtomicBoolean();
    private final AtomicBoolean chatListReceived = new AtomicBoolean();
    private List<TelegramContent> telegramContentList = new ArrayList<>();
    @Autowired
    private ConfigurationData configurationData;

    public static Client getClient() {
        return client;
    }

    @Override
    public boolean readChatsListId(int limit) {
        AtomicBoolean result = new AtomicBoolean(true);
        while (!authorized()) {
            waitAuthorization();
        }
        chatListReceived.set(false);
        client.send(new TdApi.GetChats(new TdApi.ChatListMain(), limit), object -> {
            TdApi.Chats chats = (TdApi.Chats) object;
            if (chats.totalCount == 0 || LongStream.of(chats.chatIds).noneMatch(x -> x == configurationData.getChatId())) {
                result.set(false);
            }
            chatListReceived.set(true);
            });
            while (!chatListReceived.get() && !Thread.currentThread().isInterrupted()) {
                synchronized (chatListReceived) {
                    try {
                        chatListReceived.wait(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
       // }
        return result.get();
    }
    @Override
    public List<TelegramContent> getChatHistory(int limit) {
        needQuit = false;
        List<TdApi.Message> messageList = new ArrayList<>();
        //AtomicReference<String> result = new AtomicReference<>("Nothing to read");
        while (!needQuit) {
            while (!authorized()) {
                waitAuthorization();
            }
            replyReceived.set(false);
            long fromId = messageList.isEmpty() ? 0 : getOldestMsgId(messageList);
            System.out.println("Mando richiesta da messaggio Id " + fromId);
            client.send(new TdApi.GetChatHistory(configurationData.getChatId(), fromId, 0, limit, true), object -> {
                TdApi.Messages messages = (TdApi.Messages) object;
                if (messages.totalCount == 0) {
                    needQuit = true;
                    assignTelegramContentList(messageList);
                    //result.set("Read " + messageList.size() + " messages");
                } else {
                    Arrays.asList(messages.messages).forEach(a -> {
                        if (messageList.stream().noneMatch(b -> b.id == a.id))
                            messageList.add(a);
                    });
                }
                replyReceived.set(true);
                synchronized (replyReceived){
                    replyReceived.notify();
                }
            });
            while (!replyReceived.get() && !Thread.currentThread().isInterrupted()) {
                synchronized (replyReceived) {
                    try {
                        replyReceived.wait(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        //return result.get();
        return telegramContentList;
    }

    private void assignTelegramContentList(List<TdApi.Message> messageListTemp) {
        for (TdApi.Message msg : messageListTemp) {
            TelegramContent temp = new TelegramContent(msg.content.toString(), msg.date);
            telegramContentList.add(temp);
        }
        System.out.println("Ho aggiunto i messaggi alla lista telegram: " + telegramContentList.size());
    }

    /* Ordina i messaggi dal più vecchio al più nuovo, dove il primo è sempre il messaggio
    * di creazione del canale (DA IGNORARE LATO CLIENT) */
    private long getOldestMsgId(List<TdApi.Message> messageList) {
        messageList.sort(Comparator.comparingInt(a -> a.date));
        return messageList.get(0).id;
    }

    @Override
    public void joinChatUnito() {
        needQuit = false;
        while (!needQuit) {
            while (!authorized()) {
                waitAuthorization();
            }
            System.out.println(configurationData.getChatUsername());
            client.send(new TdApi.SearchPublicChat(configurationData.getChatUsername()), object -> System.out.println(object.toString()));
            client.send(new TdApi.JoinChat(configurationData.getChatId()), object -> System.out.println(object.toString()));
            needQuit = true;
        }
        System.out.println("Mi sono iscritto al canale");
    }
    @Override
    public void update(TdApi.AuthorizationState authorizationState) {
        if (authorizationState != null) {
            this.authorizationState = authorizationState;
        }
        switch (authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                TdApi.SetTdlibParameters request = new TdApi.SetTdlibParameters();
                request.databaseDirectory = "tdlib";
                request.useMessageDatabase = true;
                request.useSecretChats = true;
                request.apiId = configurationData.getApiKey();
                request.apiHash = configurationData.getApiHash();
                request.systemLanguageCode = "en";
                request.deviceModel = "Desktop";
                request.applicationVersion = "1.0";
                request.enableStorageOptimizer = true;
                client.send(request, new AutorizationUpdatehandler(this));
                break;
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR: {
                client.send(new TdApi.SetAuthenticationPhoneNumber(configurationData.getPhonenumber(), null), new AutorizationUpdatehandler(this));
                break;
            }
            case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR: {
                String link = ((TdApi.AuthorizationStateWaitOtherDeviceConfirmation) authorizationState).link;
                System.out.println("Please confirm this login link on another device: " + link);
                break;
            }
            case TdApi.AuthorizationStateWaitEmailAddress.CONSTRUCTOR: {
                String emailAddress = SimpleHelper.promptString("Please enter email address: ");
                client.send(new TdApi.SetAuthenticationEmailAddress(emailAddress), new AutorizationUpdatehandler(this));
                break;
            }
            case TdApi.AuthorizationStateWaitEmailCode.CONSTRUCTOR: {
                String code = SimpleHelper.promptString("Please enter email authentication code: ");
                client.send(new TdApi.CheckAuthenticationEmailCode(new TdApi.EmailAddressAuthenticationCode(code)), new AutorizationUpdatehandler(this));
                break;
            }
            case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR: {
                String code = SimpleHelper.promptString("Please enter authentication code: ");
                client.send(new TdApi.CheckAuthenticationCode(code), new AutorizationUpdatehandler(this));
                break;
            }
            case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR: {
                String firstName = SimpleHelper.promptString("Please enter your first name: ");
                String lastName = SimpleHelper.promptString("Please enter your last name: ");
                client.send(new TdApi.RegisterUser(firstName, lastName), new AutorizationUpdatehandler(this));
                break;
            }
            case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR: {
                String password = SimpleHelper.promptString("Please enter password: ");
                client.send(new TdApi.CheckAuthenticationPassword(password), new AutorizationUpdatehandler(this));
                break;
            }
            case TdApi.AuthorizationStateReady.CONSTRUCTOR: {
                authorizationLock.lock();
                try {
                    gotAuthorization.signal();
                } finally {
                    authorizationLock.unlock();
                }
                break;
            }
            case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR: {
                System.out.println("Logging out");
                break;
            }
            case TdApi.AuthorizationStateClosing.CONSTRUCTOR: {
                System.out.println("Closing");
                break;
            }
            case TdApi.AuthorizationStateClosed.CONSTRUCTOR: {
                System.out.println("Closed");
                break;
            }
            default:
                System.err.println("Unsupported authorization state:\n" + authorizationState);
        }
    }
    private boolean authorized() {
        authorizationLock.lock();
        try {
            return authorizationState != null && authorizationState.getConstructor() == TdApi.AuthorizationStateReady.CONSTRUCTOR;
        } finally {
            authorizationLock.unlock();
        }
    }
    private void waitAuthorization() {
        try {
            authorizationLock.lock();
            while (!authorized() && !Thread.currentThread().isInterrupted()) {
                try {
                    gotAuthorization.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally { authorizationLock.unlock(); }
    }
    public void createClient() {
        if(client != null) {
            return;
        }
        Client.setLogMessageHandler(1, new LogMessageHandler());
        Client.execute(new TdApi.SetLogVerbosityLevel(0));
        if (Client.execute(new TdApi.SetLogStream(new TdApi.LogStreamFile("tdlib.log", 1 << 27, false))) instanceof TdApi.Error) {
            throw new IOError(new IOException("Write access to the current directory is required"));
        }
        client = Client.create(new AutorizationUpdatehandler(this), null, null);
    }
}