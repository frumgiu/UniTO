apk update
apk upgrade
apk add --update alpine-sdk linux-headers git zlib-dev openssl-dev gperf php cmake openjdk8
git clone https://github.com/tdlib/td.git
cd td
rm -rf build
mkdir build
cd build
cmake -DCMAKE_BUILD_TYPE=Release -DJAVA_HOME=/usr/lib/jvm/java-1.8-openjdk/ -DCMAKE_INSTALL_PREFIX:PATH=../example/java/td -DTD_ENABLE_JNI=ON ..
cmake --build . --target prepare_cross_compiling
cd ..
php SplitSource.php
cd build
cmake --build . --target install
cd ..
cd example/java
rm -rf build
mkdir build
cd build
cmake -DCMAKE_BUILD_TYPE=Release -DJAVA_HOME=/usr/lib/jvm/java-1.8-openjdk/ -DCMAKE_INSTALL_PREFIX:PATH=../../../tdlib -DTd_DIR:PATH=$(readlink -f ../td/lib/cmake/Td) ..
cmake --build . --target install
cd ../../..
php SplitSource.php --undo
cd ..
ls -l td/tdlib