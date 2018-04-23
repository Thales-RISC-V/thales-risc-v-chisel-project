base_dir=$(abspath .)
sim_dir=$(abspath .)

PROJECT ?= antmicro_ft
MODEL ?= FPGATop
#TestHarness
CONFIG ?= AntmicroConfig
CFG_PROJECT ?= $(PROJECT)
TB ?= TestDriver

all: verilog

include $(base_dir)/Makefrag

verilog : $(build_dir)/$(PROJECT).$(MODEL).$(CONFIG).v
