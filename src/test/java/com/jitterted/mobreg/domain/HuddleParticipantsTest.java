package com.jitterted.mobreg.domain;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.*;

public class HuddleParticipantsTest {

    @Test
    public void newHuddleHasZeroParticipants() throws Exception {
        Huddle huddle = new Huddle("test", ZonedDateTime.now());

        assertThat(huddle.numberRegistered())
                .isZero();
        assertThat(huddle.participants())
                .isEmpty();
    }

    @Test
    public void addOneParticipantToHuddleRemembersTheParticipant() throws Exception {
        Huddle huddle = new Huddle("huddle", ZonedDateTime.now());

        Member member = new Member("name", "github");
        huddle.register(member);

        assertThat(huddle.numberRegistered())
                .isEqualTo(1);
        assertThat(huddle.participants())
                .containsOnly(member);
    }

    @Test
    public void participantInHuddleIsRegisteredByUsername() throws Exception {
        Huddle huddle = createHuddleWithParticipantUsername("participant_username");

        assertThat(huddle.isRegisteredByUsername("participant_username"))
                .isTrue();
    }

    @Test
    public void participantNotInHuddleIsNotRegistered() throws Exception {
        Huddle huddle = createHuddleWithParticipantUsername("participant");

        assertThat(huddle.isRegisteredByUsername("someone_else"))
                .isFalse();
    }

    @NotNull
    private Huddle createHuddleWithParticipantUsername(String participantUsername) {
        Huddle huddle = new Huddle("huddle", ZonedDateTime.now());

        Member member = new Member("name", participantUsername);
        huddle.register(member);
        return huddle;
    }
}
