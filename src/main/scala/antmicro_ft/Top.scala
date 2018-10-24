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

//import chisel3._
import chisel3.core._
import freechips.rocketchip.coreplex._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.config.Parameters
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.faulttolerant._
import freechips.rocketchip.diplomacy._
import testchipip._


trait ExternalResetVectorImp extends LazyModuleImp {
  val switch_reset_vector = IO(Input(Bool()))
}

class AntmicroTop(implicit p: Parameters) extends RocketCoreplex
    with HasPeripheryDebug
    with HasMasterAXI4MMIOPort
    with HasPeripheryBootROM
    with HasPeripherySerial {
  override lazy val module = new AntmicroTopModule(this)
}

class AntmicroTopModule[+L <: AntmicroTop](l: L) extends RocketCoreplexModule(l)
    with HasRTCModuleImp
    with HasPeripheryDebugModuleImp
    with HasMasterAXI4MMIOPortModuleImp
    with HasPeripheryBootROMModuleImp
    with HasPeripherySerialModuleImp
    with ExternalResetVectorImp
    {
      global_reset_vector := Mux(switch_reset_vector, 0x10040.U, 0x10000.U)
    }

class TopWithBlockDevice(implicit p: Parameters) extends AntmicroTop
    with HasPeripheryBlockDevice {
  override lazy val module = new TopWithBlockDeviceModule(this)
}

class TopWithBlockDeviceModule(l: TopWithBlockDevice)
  extends AntmicroTopModule(l)
  with HasPeripheryBlockDeviceModuleImp
