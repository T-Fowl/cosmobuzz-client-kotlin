package com.tfowl.cosmobuzz.fakes

import com.tfowl.cosmobuzz.CosmoSocket
import com.tfowl.cosmobuzz.CosmoSocketFactory

class FakeCosmoSocketFactory(val socket: FakeCosmoSocket) : CosmoSocketFactory {
    override suspend fun create(): CosmoSocket {
        return socket
    }
}