package com.example.demotelegramapi.service;

import com.example.demotelegramapi.configuration.ConfigurationData;
import com.example.demotelegramapi.helpers.SimpleHelper;
import com.example.demotelegramapi.handlers.*;
import org.drinkless.tdlib.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private final AtomicBoolean historyReceived = new AtomicBoolean();
    @Autowired
    private ConfigurationData configurationData;

    public static Client getClient() {
        return client;
    }

    @Override
    public boolean readChatsListId(int limit) {
        createClient();
        AtomicBoolean result = new AtomicBoolean(true);
        while (!needQuit) {
            while (!authorized()) {
                waitAuthorization();
            }
            client.send(new TdApi.GetChats(new TdApi.ChatListMain(), limit), object -> {
                TdApi.Chats chats = (TdApi.Chats) object;
                if (chats.totalCount == 0 || LongStream.of(chats.chatIds).noneMatch(x -> x == configurationData.getChatId())) {
                    result.set(false);
                }
            });
            needQuit = true;
        }
        return result.get();
    }
    @Override
    public String getChatHistory(int limit) {
        createClient();
        AtomicReference<String> result = new AtomicReference<>("Nothing to read");
        while (!needQuit) {
            while (!authorized()) {
                waitAuthorization();
            }
            historyReceived.set(false);
            //TODO recursive function ?
            client.send(new TdApi.GetChatHistory(configurationData.getChatId(), 0, 0, limit, true), object -> {
                TdApi.Messages messages = (TdApi.Messages) object;
                result.set("Read " + messages.totalCount + " messages");
                historyReceived.set(true);
                needQuit = true;
                synchronized (historyReceived){
                    historyReceived.notify();
                }
            });
            while (!historyReceived.get() && !Thread.currentThread().isInterrupted()) {
                synchronized (historyReceived) {
                    try {
                        historyReceived.wait(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return result.get();
    }
    @Override
    public void joinChatUnito() {
        createClient();
        while (!needQuit) {
            while (!authorized()) {
                waitAuthorization();
            }
            System.out.println(configurationData.getChatUsername());
            client.send(new TdApi.SearchPublicChat(configurationData.getChatUsername()), object -> System.out.println("Find it."));
            client.send(new TdApi.JoinChat(configurationData.getChatId()), object -> System.out.println(object.toString()));
            needQuit = true;
        }
    }

    @Override
    public void update(TdApi.AuthorizationState authorizationState) {
        if (authorizationState != null) {
            this.authorizationState = authorizationState;
        }
        switch (authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
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
            }
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                client.send(new TdApi.SetAuthenticationPhoneNumber(configurationData.getPhonenumber(), null), new AutorizationUpdatehandler(this));
            }
            case TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> {
                String link = ((TdApi.AuthorizationStateWaitOtherDeviceConfirmation) authorizationState).link;
                System.out.println("Please confirm this login link on another device: " + link);
            }
            case TdApi.AuthorizationStateWaitEmailAddress.CONSTRUCTOR -> {
                String emailAddress = SimpleHelper.promptString("Please enter email address: ");
                client.send(new TdApi.SetAuthenticationEmailAddress(emailAddress), new AutorizationUpdatehandler(this));
            }
            case TdApi.AuthorizationStateWaitEmailCode.CONSTRUCTOR -> {
                String code = SimpleHelper.promptString("Please enter email authentication code: ");
                client.send(new TdApi.CheckAuthenticationEmailCode(new TdApi.EmailAddressAuthenticationCode(code)), new AutorizationUpdatehandler(this));
            }
            case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                String code = SimpleHelper.promptString("Please enter authentication code: ");
                client.send(new TdApi.CheckAuthenticationCode(code), new AutorizationUpdatehandler(this));
            }
            case TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR -> {
                String firstName = SimpleHelper.promptString("Please enter your first name: ");
                String lastName = SimpleHelper.promptString("Please enter your last name: ");
                client.send(new TdApi.RegisterUser(firstName, lastName), new AutorizationUpdatehandler(this));
            }
            case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                String password = SimpleHelper.promptString("Please enter password: ");
                client.send(new TdApi.CheckAuthenticationPassword(password), new AutorizationUpdatehandler(this));
            }
            case TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                authorizationLock.lock();
                try {
                    gotAuthorization.signal();
                } finally {
                    authorizationLock.unlock();
                }
            }
            case TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR -> {
                System.out.println("Logging out");
            }
            case TdApi.AuthorizationStateClosing.CONSTRUCTOR -> {
                System.out.println("Closing");
            }
            case TdApi.AuthorizationStateClosed.CONSTRUCTOR -> {
                System.out.println("Closed");
            }
            default -> System.err.println("Unsupported authorization state:\n" + authorizationState);
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
    private void createClient() {
        if(client != null) {
            needQuit = false;
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