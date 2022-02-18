package com.jitterted.mobreg.adapter.out.jdbc;

import com.jitterted.mobreg.domain.Ensemble;
import com.jitterted.mobreg.domain.MemberId;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class EnsembleDboMappingTest {

    @Test
    public void databaseEntityToDomainIsMappedCorrectly() throws Exception {
        EnsembleDbo ensembleDbo = new EnsembleDbo();
        ensembleDbo.setId(19L);
        ensembleDbo.setState("CANCELED");
        ensembleDbo.setRecordingLink("https://recording.link/entity");
        ZonedDateTime now = ZonedDateTime.now();
        ensembleDbo.setDateTimeUtc(now.toLocalDateTime());
        ensembleDbo.setName("Entity");
        ensembleDbo.setZoomMeetingLink("https://zoom.us/entity");
        ensembleDbo.setAcceptedMembers(Set.of(new AcceptedMember(13L)));
        ensembleDbo.setDeclinedMembers(Set.of(new DeclinedMember(29L)));

        Ensemble ensemble = ensembleDbo.asEnsemble();

        assertThat(ensemble.isCompleted())
                .isFalse();
        assertThat(ensemble.isCanceled())
                .isTrue();
        assertThat(ensemble.name())
                .isEqualTo("Entity");
        assertThat(ensemble.recordingLink().toString())
                .isEqualTo("https://recording.link/entity");
        assertThat(ensemble.zoomMeetingLink().toString())
                .isEqualTo("https://zoom.us/entity");
        assertThat(ensemble.acceptedMembers())
                .extracting(MemberId::id)
                .isEqualTo(List.of(13L));
        assertThat(ensemble.declinedMembers())
                .extracting(MemberId::id)
                .isEqualTo(List.of(29L));
    }

    @Test
    public void domainToDatabaseEntityIsMappedCorrectly() throws Exception {
        ZonedDateTime utc2021091316000 = ZonedDateTime.of(2021, 9, 13, 16, 0, 0, 0, ZoneOffset.UTC);
        Ensemble ensemble = new Ensemble("Domain", URI.create("https://zoom.us/"), utc2021091316000);
        ensemble.linkToRecordingAt(URI.create("https://recording.link/domain"));
        ensemble.acceptedBy(MemberId.of(11L));
        ensemble.declinedBy(MemberId.of(13L));
        ensemble.complete();

        EnsembleDbo entity = EnsembleDbo.from(ensemble);

        assertThat(entity.getName())
                .isEqualTo("Domain");
        assertThat(entity.getDateTimeUtc())
                .isEqualTo(utc2021091316000.toLocalDateTime());
        assertThat(entity.getState())
                .isEqualTo("COMPLETED");
        assertThat(entity.getRecordingLink())
                .isEqualTo("https://recording.link/domain");
        assertThat(entity.getAcceptedMembers())
                .extracting(AcceptedMember::asMemberId)
                .extracting(MemberId::id)
                .isEqualTo(List.of(11L));
        assertThat(entity.getDeclinedMembers())
                .extracting(DeclinedMember::asMemberId)
                .extracting(MemberId::id)
                .isEqualTo(List.of(13L));
    }

}