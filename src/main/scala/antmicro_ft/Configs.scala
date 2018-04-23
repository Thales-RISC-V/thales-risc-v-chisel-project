package antmicro_ft

import chisel3._
import freechips.rocketchip.config._
import freechips.rocketchip.coreplex._
import freechips.rocketchip.devices.debug._
import freechips.rocketchip.devices.tilelink._
import freechips.rocketchip.diplomacy._
import freechips.rocketchip.rocket._
import freechips.rocketchip.tile._
import freechips.rocketchip.tilelink._
import freechips.rocketchip.util._
import freechips.rocketchip.system.{BaseConfig}
import testchipip._

class WithBootROM extends Config((site, here, up) => {
  case BootROMParams => BootROMParams(
    contentFileName = s"./bootrom/bootrom.rv32.img")
})

class WithAntmicroTop extends Config((site, here, up) => {
  case BuildTop => (clock: Clock, reset: Bool, p: Parameters) =>
    Module(LazyModule(new AntmicroTop()(p)).module)
})

class WithBlockDeviceModel extends Config((site, here, up) => {
  case BuildTop => (clock: Clock, reset: Bool, p: Parameters) => {
    val top = Module(LazyModule(new TopWithBlockDevice()(p)).module)
    top.connectBlockDeviceModel()
    top
  }
})

class WithNTinyCoresNoCache(n: Int) extends Config((site, here, up) => {
    case XLen => 32
    case RocketTilesKey => {
      val tiny = RocketTileParams(
        core = RocketCoreParams(
          useVM = false,
          fpu = None,
          mulDiv = Some(MulDivParams(mulUnroll = 8))),
        btb = None,
        dcache =Some(DCacheParams(
          rowBits = site(SystemBusKey).beatBits,
          nSets = 256, // 16Kb scratchpad
          nWays = 1,
          nTLBEntries = 4,
          nMSHRs = 0,
          blockBytes = site(CacheBlockBytes),
          scratch = Some(0x40000000L))),
        icache = Some(ICacheParams(
          rowBits = site(SystemBusKey).beatBits,
          nSets = 64,
          nWays = 1,
          nTLBEntries = 4,
          blockBytes = site(CacheBlockBytes))))
    List.tabulate(n)(i => tiny.copy(hartid = i))
  }
})

class AntmicroPeriphConfig extends Config((site, here, up) =>
  {
    case ExtMem => MasterPortParams(
      base = x"8000_0000",
      size = x"2000_0000",
      beatBytes = site(MemoryBusKey).beatBytes,
      idBits = 4)

  }

  )

class AntmicroConfig extends Config(
  new WithAntmicroTop ++
  new AntmicroPeriphConfig ++
  new WithBootROM ++
  new WithNMemoryChannels(0) ++
  new WithStatelessBridge ++
//  new WithNExtTopInterrupts(0) ++
  new WithJtagDTM ++
  new WithNTinyCoresNoCache(1) ++
  new BaseConfig)
