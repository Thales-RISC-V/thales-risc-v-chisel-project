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
