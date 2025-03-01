import de.maxhenkel.voicechat.api.ServerPlayer;
import de.maxhenkel.voicechat.api.audiolistener.PlayerAudioListener;
import de.maxhenkel.voicechat.api.packets.SoundPacket;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class VoiceChatListenerPlugin implements VoicechatPlugin {

    @Override
    public String getPluginId() {
        return "voice_chat_listener_plugin";
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onVoicechatServerStarted);
    }

    private void onVoicechatServerStarted(VoicechatServerStartedEvent event) {
        VoicechatServerApi api = event.getVoicechat();

        // Example: Listen to a specific player's voice packets
        UUID playerUuid = UUID.fromString("player-uuid-here"); // Replace with the actual player UUID

        PlayerAudioListener listener = api.playerAudioListenerBuilder()
                .setPlayer(playerUuid)
                .setPacketListener(this::handleSoundPacket)
                .build();

        api.registerAudioListener(listener);
    }

    private void handleSoundPacket(SoundPacket soundPacket) {
        // Handle the incoming sound packet
        System.out.println("Received sound packet from player: " + soundPacket.getSender().getClass());
        // You can process the sound packet data here
    }
}