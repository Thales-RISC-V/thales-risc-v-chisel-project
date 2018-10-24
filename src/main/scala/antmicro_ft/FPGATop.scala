/*  
*   Copyright © 2018. Thales SA 
*   All rights reserved
*   
*   Redistribution and use in source and binary forms, with or without
*   modification, are permitted provided that the following conditions
*   are met:
*   
*   1. Redistributions of source code must retain the above copyright
*      notice, this list of conditions and the following disclaimer.
*   
*   2. Redistributions in binary form must reproduce the above copyright
*      notice, this list of conditions and the following disclaimer in the
*      documentation and/or other materials provided with the distribution.
*   
*   3. Neither the name of the copyright holders nor the names of its
*      contributors may be used to endorse or promote products derived from
*      this software without specific prior written permission.
*   
*   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
*   AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
*   IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
*   ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
*   LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
*   CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
*   SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
*   INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
*   CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
*   ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
*   THE POSSIBILITY OF SUCH DAMAGE.
*/

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

