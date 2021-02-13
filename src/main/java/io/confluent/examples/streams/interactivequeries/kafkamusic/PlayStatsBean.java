package io.confluent.examples.streams.interactivequeries.kafkamusic;

import java.util.List;
import java.util.Objects;

/**
 * PlayStatsBean holds a list of songs and their counts, as well as the number of songs that have been played.
 * An aggregate of the total play count for all songs is also computed and held in this class
 *
 * @author kunaalkumar
 */
public class PlayStatsBean {
    private long totalPlays = 0;
    private long numOfSongsPlayed = 0;
    private List<SongPlayCountBean> songs;

    public PlayStatsBean() {
    }

    public PlayStatsBean(final List<SongPlayCountBean> songs) {
        this.songs = songs;
        songs.forEach((song) -> totalPlays += song.getPlays());
        this.numOfSongsPlayed = songs.size();
    }

    public long getTotalPlays() {
        return totalPlays;
    }

    public void setTotalPlays(final long totalPlays) {
        this.totalPlays = totalPlays;
    }

    public long getNumOfSongsPlayed() {
        return numOfSongsPlayed;
    }

    public void setNumOfSongsPlayed(final long numOfSongsPlayed) {
        this.numOfSongsPlayed = numOfSongsPlayed;
    }

    public List<SongPlayCountBean> getSongs() {
        return songs;
    }

    public void setSongs(final List<SongPlayCountBean> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "PlayStatsBean{" +
                "totalPlays=" + totalPlays +
                ", numOfSongsPlayed=" + songs.size() +
                ", songs=" + songs +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PlayStatsBean that = (PlayStatsBean) o;
        return totalPlays == that.totalPlays && Objects.equals(songs, that.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPlays, songs);
    }
}