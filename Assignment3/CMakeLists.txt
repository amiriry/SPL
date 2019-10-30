cmake_minimum_required(VERSION 3.12)
project(assignment3Client)

set(CMAKE_CXX_STANDARD 14)

find_package(Boost COMPONENTS system filesystem REQUIRED)
include_directories(${Boost_INCLUDE_DIRS})

include_directories(include)
include_directories(src)
add_executable(assignment3Client
        include/connectionHandler.h
        include/EncoderDecoder.h
        src/BGSClient.cpp
        src/connectionHandler.cpp
        src/EchoClient.cpp
        src/EncoderDecoder.cpp
        src/Main.cpp)

target_link_libraries(assignment3Client ${Boost_LIBRARIES})
cmake_minimum_required(VERSION 3.10)
project(assignment3Client)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -pthread -lboost_system")

find_package(Boost 1.58.0 REQUIRED COMPONENTS
        filesystem thread)

if(Boost_FOUND)
    include_directories(${Boost_INCLUDE_DIRS})
    set (RESOURCE src/BGSClient.cpp  src/connectionHandler.cpp  src/EchoClient.cpp  src/EncoderDecoder.cpp  src/Main.cpp include/connectionHandler.h  include/EncoderDecoder.h)
    add_executable(Client ${RESOURCE})
    target_link_libraries(Client ${Boost_LIBRARIES})
endif()
