package io.confluent.examples.streams.interactivequeries.kafkamusic;

import io.confluent.examples.streams.avro.SongPlayCount;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Used in aggregations to keep track of Song play counts.
 */
public class SongStats implements Iterable<SongPlayCount> {
    // <Song id, song play count>
    private final HashMap<Long, SongPlayCount> songsPlayed = new HashMap<>();

    @Override
    public String toString() {
        return songsPlayed.toString();
    }

    public void add(final SongPlayCount songPlayCount) {
        songsPlayed.put(songPlayCount.getSongId(), songPlayCount);
    }

    void remove(final SongPlayCount value) {
        songsPlayed.remove(value.getSongId());
    }

    @Override
    public Iterator<SongPlayCount> iterator() {
        return songsPlayed.values().iterator();
    }

    static class SongStatsSerde implements Serde<SongStats> {

        @Override
        public Serializer<SongStats> serializer() {
            return (s, songStats) -> {
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                final DataOutputStream dataOutputStream = new DataOutputStream(out);
                try {
                    for (final SongPlayCount playCount : songStats) {
                        dataOutputStream.writeLong(playCount.getSongId());
                        dataOutputStream.writeLong(playCount.getPlays());
                    }
                    dataOutputStream.flush();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
                return out.toByteArray();
            };
        }

        @Override
        public Deserializer<SongStats> deserializer() {
            return (s, bytes) -> {
                if (bytes == null || bytes.length == 0)
                    return null;
                final SongStats songStats = new SongStats();

                final DataInputStream
                        dataInputStream =
                        new DataInputStream(new ByteArrayInputStream(bytes));
                try {
                    while (dataInputStream.available() > 0) {
                        songStats.add(new SongPlayCount(dataInputStream.readLong(), dataInputStream.readLong()));
                    }
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
                return songStats;
            };
        }
    }
}