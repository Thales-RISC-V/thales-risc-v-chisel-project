package freechips.rocketchip.faulttolerant

import chisel3.core._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.rocket._
import freechips.rocketchip.tile._
import freechips.rocketchip.config.Parameters

trait HasFaultTolerantPort {
  val overrideTvec = Input(UInt(1.W))
}

trait HasFaultTolerantPortImp extends LazyModuleImp{
  val outer: HasFaultTolerantPort
  val overrideTvec = IO(Input(UInt(1.W)))
}

trait HasPCPort {
  val pc = Output(UInt(32.W))
}

trait HasPCPortImp extends LazyModuleImp
{
  val pc = IO(Output(UInt(32.W)))
}

trait HasMStatusPortImp extends LazyModuleImp {
  val mstatus = IO(Output(new MStatus))
}

trait HasMStatusPort {
  val mstatus = Output(new MStatus)
}

trait HasRegisterPorts {
  val regs = Output(new RegisterPorts)
}

class RegisterPorts extends Bundle{
  val ra = UInt(32.W)
  val sp = UInt(32.W)
  val gp = UInt(32.W)
  val tp = UInt(32.W)
  val t0 = UInt(32.W)
  val t1 = UInt(32.W)
  val t2 = UInt(32.W)
  val fp = UInt(32.W)
  val s1 = UInt(32.W)
  val a0 = UInt(32.W)
  val a1 = UInt(32.W)
  val a2 = UInt(32.W)
  val a3 = UInt(32.W)
  val a4 = UInt(32.W)
  val a5 = UInt(32.W)
  val a6 = UInt(32.W)
  val a7 = UInt(32.W)
  val s2 = UInt(32.W)
  val s3 = UInt(32.W)
  val s4 = UInt(32.W)
  val s5 = UInt(32.W)
  val s6 = UInt(32.W)
  val s7 = UInt(32.W)
  val s8 = UInt(32.W)
  val s9 = UInt(32.W)
  val s10 = UInt(32.W)
  val s11 = UInt(32.W)
  val t3 = UInt(32.W)
  val t4 = UInt(32.W)
  val t5 = UInt(32.W)
  val t6 = UInt(32.W)
  val pc = UInt(32.W)
}

trait HasRegisterPortsImp extends LazyModuleImp {
  val regs = IO(Output(new RegisterPorts))
}
