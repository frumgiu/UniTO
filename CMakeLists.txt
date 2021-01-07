cmake_minimum_required(VERSION 3.17)
project(TaxiCab C)

set(CMAKE_C_STANDARD 90)
add_compile_options(-ansi -pedantic -Werror)
add_executable(TaxiCab main.c configurazione.c cella.c mappa.c client.c Common_IPC.c taxi.c)