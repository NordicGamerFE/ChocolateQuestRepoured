package com.teamcqr.chocolatequestrepoured.network.packets.toClient;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SPacketUpdateTradeIndex implements IMessage {

	private int entityId;
	private int tradeIndex;
	private int newTradeIndex;

	public SPacketUpdateTradeIndex() {

	}

	public SPacketUpdateTradeIndex(int entityId, int tradeIndex, int newTradeIndex) {
		this.entityId = entityId;
		this.tradeIndex = tradeIndex;
		this.newTradeIndex = newTradeIndex;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.tradeIndex = buf.readInt();
		this.newTradeIndex = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.tradeIndex);
		buf.writeInt(this.newTradeIndex);
	}

	public int getEntityId() {
		return this.entityId;
	}

	public int getTradeIndex() {
		return this.tradeIndex;
	}

	public int getNewTradeIndex() {
		return this.newTradeIndex;
	}

}
