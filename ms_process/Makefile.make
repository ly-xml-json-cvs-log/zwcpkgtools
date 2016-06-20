#######################################################################
# 需要生成的目标文件
TARGET_BIN +=
TARGET_BIN := $(addprefix bin/, $(TARGET_BIN))

TARGET_LIB +=
TARGET_SO  +=
TARGET_LIB := $(addprefix lib/, $(TARGET_LIB))
TARGET_SO  := $(addprefix lib/, $(TARGET_SO))
########################################################################
SUBDIS_BD  += 

OBJ        := obj
LIB        := lib
BIN        := bin

KEY_DIRS   += $(OBJ) $(LIB) $(BIN)

########################################################################
# 需要依赖的文件, 包括*.so, *.a, 等等
SOURCE_SO  +=
SOURCE_LIB += 
SOURCE_INC +=
SOURCE_C   +=
SOURCE_CC  += 
SOURCE_CPP += 
SOURCE_OBJ += $(patsubst %.cc, $(OBJ)/%.o, $(notdir $(SOURCE_CC))) $(patsubst %.cpp, $(OBJ)/%.o, $(notdir $(SOURCE_CPP))) $(patsubst %.c, $(OBJ)/%.o, $(notdir $(SOURCE_C)))

########################################################################
# 系统的路径依赖
DEPENT_INC += -I/usr/local/include
DEPENT_LIB += -L./lib/
########################################################################
# 编译需要的选项
CCFLAGS    += 
CPPFLAGS   += 
CFLAGS     += -std=c++11 -ldl -Wall
SOFLAGS    += -shared -fPIC
GCC        := g++
RM	   := rm
AR	   := ar
ARFLAG     := -crs
MKDIR      := mkdir
########################################################################
ifneq ($(strip $(CFLAGS)), )
MKFLAGS += $(CFLAGS)
endif

ifneq ($(strip $(CCFLAGS)), )
MKFLAGS += $(CCFLAGS)
endif

ifneq ($(strip $(CPPFLAGS)), )
MKFLAGS += $(CPPFLAGS)
endif

ifneq ($(strip $(DEPENT_INC)), )
MKFLAGS += $(DEPENT_INC)
endif

ifneq ($(strip $(DEPENT_LIB)), )
MKFLAGS += $(DEPENT_LIB)
endif

ifneq ($(strip $(SOURCE_LIB)), )
MKFLAGS += $(SOURCE_LIB)
endif

ifneq ($(strip $(SOURCE_SO)), )
MKFLAGS += $(SOURCE_SO)
endif

.PHONY: all


all: $(KEY_DIRS) $(SOURCE_OBJ) $(TARGET_BIN) $(TARGET_SO) $(TARGET_LIB)

ifneq ($(strip $(KEY_DIRS)), )
$(KEY_DIRS): 
	$(MKDIR) -p $@
endif

ifneq ($(strip $(SOURCE_CC)), )
$(OBJ)/%.o : %.cc
	$(GCC) $(MKFLAGS) -c $< -o $@
endif

ifneq ($(strip $(SOURCE_CPP)), )
$(OBJ)/%.o : %.cpp
	$(GCC) $(MKFLAGS) -c $< -o $@
endif

ifneq ($(strip $(SUBDIS_BD)), )
$(SUBDIS_BD) : $(SOURCE_OBJ)
	$(GCC) $(MKFLAGS) -o $@ $^
endif

ifneq ($(strip $(TARGET_SO)), )
$(TARGET_SO) : $(SOURCE_OBJ)
	$(GCC) $(MKFLAGS) $(SOFLAGS) -o $@ $^
endif

ifneq ($(strip $(TARGET_LIB)), )
$(TARGET_LIB) : $(SOURCE_OBJ)
	$(AR) $(ARFLAG) $@ $^
endif

ifneq ($(strip $(TARGET_BIN)), )
$(TARGET_BIN) : $(SOURCE_OBJ)
	$(GCC) $(MKFLAGS) -o $@ $^
endif

clean:
	$(RM) -rf $(SOURCE_OBJ) $(TARGET_BIN) $(TARGET_LIB) $(TARGET_SO)

cleanall:
	$(RM) -rf $(KEY_DIRS)
