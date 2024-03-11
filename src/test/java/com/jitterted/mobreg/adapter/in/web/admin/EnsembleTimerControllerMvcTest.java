package com.jitterted.mobreg.adapter.in.web.admin;

import com.jitterted.mobreg.adapter.in.web.TestAdminConfiguration;
import com.jitterted.mobreg.application.TestEnsembleServiceBuilder;
import com.jitterted.mobreg.application.port.EnsembleRepository;
import com.jitterted.mobreg.domain.Ensemble;
import com.jitterted.mobreg.domain.EnsembleBuilder;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("mvc")
@WebMvcTest({EnsembleTimerController.class})
@Import(TestAdminConfiguration.class)
@WithMockUser(username = "admin", authorities = {"ROLE_MEMBER", "ROLE_ADMIN"})
class EnsembleTimerControllerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EnsembleRepository ensembleRepository;

    @Test
    void postToTimerViewRedirects() throws Exception {
        createAndSaveEnsembleInRepositoryForId(113);
        mockMvc.perform(post("/admin/timer-view/113")
                                .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/timer-view/113"));
    }

    @Test
    void getForTimerViewEndpointReturns200OK() throws Exception {
        createAndStartTimerForEnsembleWithId(113);

        mockMvc.perform(get("/admin/timer-view/113"))
               .andExpect(status().isOk());
    }

    @Test
    void postToStartTimerEndpointReturns204NoContent() throws Exception {
        createAndStartTimerForEnsembleWithId(113);

        mockMvc.perform(post("/admin/start-timer/113")
                                .with(csrf()))
               .andExpect(status().isNoContent());
    }

    @Test
    void postToNextRotationEndpointReturns204NoContent() throws Exception {
        createAndStartTimerForEnsembleWithId(375);

        mockMvc.perform(post("/admin/rotate-timer/375").with(csrf()))
                .andExpect(status().isNoContent());
    }

    private void createAndStartTimerForEnsembleWithId(int ensembleId) throws Exception {
        createAndSaveEnsembleInRepositoryForId(ensembleId);
        mockMvc.perform(post("/admin/timer-view/113").with(csrf()));
    }

    private void createAndSaveEnsembleInRepositoryForId(long ensembleId) {
        Ensemble ensemble = new EnsembleBuilder().id(ensembleId)
                                                 .startsNow()
                                                 .build();
        new TestEnsembleServiceBuilder()
                .withEnsembleRepository(ensembleRepository)
                .saveEnsemble(ensemble)
                .withThreeParticipants();
    }
}