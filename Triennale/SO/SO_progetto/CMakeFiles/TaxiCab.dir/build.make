# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.17

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Disable VCS-based implicit rules.
% : %,v


# Disable VCS-based implicit rules.
% : RCS/%


# Disable VCS-based implicit rules.
% : RCS/%,v


# Disable VCS-based implicit rules.
% : SCCS/s.%


# Disable VCS-based implicit rules.
% : s.%


.SUFFIXES: .hpux_make_needs_suffix_list


# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /snap/clion/138/bin/cmake/linux/bin/cmake

# The command to remove a file.
RM = /snap/clion/138/bin/cmake/linux/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/giulia/CLionProjects/TaxiCab

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/giulia/CLionProjects/TaxiCab

# Include any dependencies generated for this target.
include CMakeFiles/TaxiCab.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/TaxiCab.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/TaxiCab.dir/flags.make

CMakeFiles/TaxiCab.dir/main.c.o: CMakeFiles/TaxiCab.dir/flags.make
CMakeFiles/TaxiCab.dir/main.c.o: main.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/giulia/CLionProjects/TaxiCab/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/TaxiCab.dir/main.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/TaxiCab.dir/main.c.o   -c /home/giulia/CLionProjects/TaxiCab/main.c

CMakeFiles/TaxiCab.dir/main.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/TaxiCab.dir/main.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/giulia/CLionProjects/TaxiCab/main.c > CMakeFiles/TaxiCab.dir/main.c.i

CMakeFiles/TaxiCab.dir/main.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/TaxiCab.dir/main.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/giulia/CLionProjects/TaxiCab/main.c -o CMakeFiles/TaxiCab.dir/main.c.s

CMakeFiles/TaxiCab.dir/configurazione.c.o: CMakeFiles/TaxiCab.dir/flags.make
CMakeFiles/TaxiCab.dir/configurazione.c.o: configurazione.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/giulia/CLionProjects/TaxiCab/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Building C object CMakeFiles/TaxiCab.dir/configurazione.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/TaxiCab.dir/configurazione.c.o   -c /home/giulia/CLionProjects/TaxiCab/configurazione.c

CMakeFiles/TaxiCab.dir/configurazione.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/TaxiCab.dir/configurazione.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/giulia/CLionProjects/TaxiCab/configurazione.c > CMakeFiles/TaxiCab.dir/configurazione.c.i

CMakeFiles/TaxiCab.dir/configurazione.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/TaxiCab.dir/configurazione.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/giulia/CLionProjects/TaxiCab/configurazione.c -o CMakeFiles/TaxiCab.dir/configurazione.c.s

CMakeFiles/TaxiCab.dir/cella.c.o: CMakeFiles/TaxiCab.dir/flags.make
CMakeFiles/TaxiCab.dir/cella.c.o: cella.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/giulia/CLionProjects/TaxiCab/CMakeFiles --progress-num=$(CMAKE_PROGRESS_3) "Building C object CMakeFiles/TaxiCab.dir/cella.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/TaxiCab.dir/cella.c.o   -c /home/giulia/CLionProjects/TaxiCab/cella.c

CMakeFiles/TaxiCab.dir/cella.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/TaxiCab.dir/cella.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/giulia/CLionProjects/TaxiCab/cella.c > CMakeFiles/TaxiCab.dir/cella.c.i

CMakeFiles/TaxiCab.dir/cella.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/TaxiCab.dir/cella.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/giulia/CLionProjects/TaxiCab/cella.c -o CMakeFiles/TaxiCab.dir/cella.c.s

CMakeFiles/TaxiCab.dir/mappa.c.o: CMakeFiles/TaxiCab.dir/flags.make
CMakeFiles/TaxiCab.dir/mappa.c.o: mappa.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/giulia/CLionProjects/TaxiCab/CMakeFiles --progress-num=$(CMAKE_PROGRESS_4) "Building C object CMakeFiles/TaxiCab.dir/mappa.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/TaxiCab.dir/mappa.c.o   -c /home/giulia/CLionProjects/TaxiCab/mappa.c

CMakeFiles/TaxiCab.dir/mappa.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/TaxiCab.dir/mappa.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/giulia/CLionProjects/TaxiCab/mappa.c > CMakeFiles/TaxiCab.dir/mappa.c.i

CMakeFiles/TaxiCab.dir/mappa.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/TaxiCab.dir/mappa.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/giulia/CLionProjects/TaxiCab/mappa.c -o CMakeFiles/TaxiCab.dir/mappa.c.s

CMakeFiles/TaxiCab.dir/client.c.o: CMakeFiles/TaxiCab.dir/flags.make
CMakeFiles/TaxiCab.dir/client.c.o: client.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/giulia/CLionProjects/TaxiCab/CMakeFiles --progress-num=$(CMAKE_PROGRESS_5) "Building C object CMakeFiles/TaxiCab.dir/client.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/TaxiCab.dir/client.c.o   -c /home/giulia/CLionProjects/TaxiCab/client.c

CMakeFiles/TaxiCab.dir/client.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/TaxiCab.dir/client.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/giulia/CLionProjects/TaxiCab/client.c > CMakeFiles/TaxiCab.dir/client.c.i

CMakeFiles/TaxiCab.dir/client.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/TaxiCab.dir/client.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/giulia/CLionProjects/TaxiCab/client.c -o CMakeFiles/TaxiCab.dir/client.c.s

CMakeFiles/TaxiCab.dir/Common_IPC.c.o: CMakeFiles/TaxiCab.dir/flags.make
CMakeFiles/TaxiCab.dir/Common_IPC.c.o: Common_IPC.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/giulia/CLionProjects/TaxiCab/CMakeFiles --progress-num=$(CMAKE_PROGRESS_6) "Building C object CMakeFiles/TaxiCab.dir/Common_IPC.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/TaxiCab.dir/Common_IPC.c.o   -c /home/giulia/CLionProjects/TaxiCab/Common_IPC.c

CMakeFiles/TaxiCab.dir/Common_IPC.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/TaxiCab.dir/Common_IPC.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/giulia/CLionProjects/TaxiCab/Common_IPC.c > CMakeFiles/TaxiCab.dir/Common_IPC.c.i

CMakeFiles/TaxiCab.dir/Common_IPC.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/TaxiCab.dir/Common_IPC.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/giulia/CLionProjects/TaxiCab/Common_IPC.c -o CMakeFiles/TaxiCab.dir/Common_IPC.c.s

CMakeFiles/TaxiCab.dir/taxi.c.o: CMakeFiles/TaxiCab.dir/flags.make
CMakeFiles/TaxiCab.dir/taxi.c.o: taxi.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/giulia/CLionProjects/TaxiCab/CMakeFiles --progress-num=$(CMAKE_PROGRESS_7) "Building C object CMakeFiles/TaxiCab.dir/taxi.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/TaxiCab.dir/taxi.c.o   -c /home/giulia/CLionProjects/TaxiCab/taxi.c

CMakeFiles/TaxiCab.dir/taxi.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/TaxiCab.dir/taxi.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/giulia/CLionProjects/TaxiCab/taxi.c > CMakeFiles/TaxiCab.dir/taxi.c.i

CMakeFiles/TaxiCab.dir/taxi.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/TaxiCab.dir/taxi.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/giulia/CLionProjects/TaxiCab/taxi.c -o CMakeFiles/TaxiCab.dir/taxi.c.s

# Object files for target TaxiCab
TaxiCab_OBJECTS = \
"CMakeFiles/TaxiCab.dir/main.c.o" \
"CMakeFiles/TaxiCab.dir/configurazione.c.o" \
"CMakeFiles/TaxiCab.dir/cella.c.o" \
"CMakeFiles/TaxiCab.dir/mappa.c.o" \
"CMakeFiles/TaxiCab.dir/client.c.o" \
"CMakeFiles/TaxiCab.dir/Common_IPC.c.o" \
"CMakeFiles/TaxiCab.dir/taxi.c.o"

# External object files for target TaxiCab
TaxiCab_EXTERNAL_OBJECTS =

TaxiCab: CMakeFiles/TaxiCab.dir/main.c.o
TaxiCab: CMakeFiles/TaxiCab.dir/configurazione.c.o
TaxiCab: CMakeFiles/TaxiCab.dir/cella.c.o
TaxiCab: CMakeFiles/TaxiCab.dir/mappa.c.o
TaxiCab: CMakeFiles/TaxiCab.dir/client.c.o
TaxiCab: CMakeFiles/TaxiCab.dir/Common_IPC.c.o
TaxiCab: CMakeFiles/TaxiCab.dir/taxi.c.o
TaxiCab: CMakeFiles/TaxiCab.dir/build.make
TaxiCab: CMakeFiles/TaxiCab.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/giulia/CLionProjects/TaxiCab/CMakeFiles --progress-num=$(CMAKE_PROGRESS_8) "Linking C executable TaxiCab"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/TaxiCab.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/TaxiCab.dir/build: TaxiCab

.PHONY : CMakeFiles/TaxiCab.dir/build

CMakeFiles/TaxiCab.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/TaxiCab.dir/cmake_clean.cmake
.PHONY : CMakeFiles/TaxiCab.dir/clean

CMakeFiles/TaxiCab.dir/depend:
	cd /home/giulia/CLionProjects/TaxiCab && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/giulia/CLionProjects/TaxiCab /home/giulia/CLionProjects/TaxiCab /home/giulia/CLionProjects/TaxiCab /home/giulia/CLionProjects/TaxiCab /home/giulia/CLionProjects/TaxiCab/CMakeFiles/TaxiCab.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/TaxiCab.dir/depend
