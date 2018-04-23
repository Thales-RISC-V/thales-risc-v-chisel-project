package antmicro_ft

import chisel3._
import chisel3.experimental.{RawModule, withClockAndReset}
import freechips.rocketchip.config.{Field, Parameters}
import freechips.rocketchip.system.Generator
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.util._

import freechips.rocketchip.amba.axi4._
import freechips.rocketchip.coreplex._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.jtag._

import testchipip.GeneratorApp

case object BuildTop extends Field[(Clock, Bool, Parameters) => AntmicroTopModule[AntmicroTop]]

class FPGATop (implicit val p: Parameters) extends Module {

    val io = IO(new Bundle {
      val mmio_axi4 = AXI4Bundle(new AXI4BundleParameters(32, 32, 4, 1))
      val jtag = new JTAGIO(hasTRSTn = false).flip
      val jtag_reset = Input(UInt(1.W))
      val switch_reset_vector = Input(UInt(1.W))
    })

    val JtagDTMKey = new JtagDTMConfig (
      idcodeVersion = 2,
      idcodePartNum = 0x000,
      idcodeManufId = 0x489,
      debugIdleCycles = 5)

    val dut = p(BuildTop)(clock, reset.toBool, p)

    dut.overrideTvec := 0.U
    dut.switch_reset_vector := io.switch_reset_vector

    io.mmio_axi4 <> dut.mmio_axi4(0)
    val sjtag = dut.debug.systemjtag.get

    sjtag.mfr_id := JtagDTMKey.idcodeManufId.U(11.W)

    io.jtag <> sjtag.jtag
    sjtag.reset := io.jtag_reset
}

object Generator extends GeneratorApp {
  generateFirrtl
}

