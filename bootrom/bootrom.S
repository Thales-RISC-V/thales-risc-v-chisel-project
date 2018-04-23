.section .text.init
.option norvc
.section .text.start, "ax", @progbits
.globl _start
_start:
  csrr a0, mhartid
  li t0, DDR_TARGET_ADDR
  jr t0

.section .text.hang, "ax", @progbits

.globl _hang
_hang:

1:
  j 1b
