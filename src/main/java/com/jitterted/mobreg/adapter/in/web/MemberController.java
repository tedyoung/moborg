package com.jitterted.mobreg.adapter.in.web;

import com.jitterted.mobreg.domain.Huddle;
import com.jitterted.mobreg.domain.HuddleId;
import com.jitterted.mobreg.domain.HuddleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {

    private final HuddleService huddleService;

    @Autowired
    public MemberController(HuddleService huddleService) {
        this.huddleService = huddleService;
    }

    @GetMapping("/member/register")
    public String showHuddlesForUser(Model model, @AuthenticationPrincipal AuthenticatedPrincipal principal) {
        MemberRegisterForm memberRegisterForm;
        String username;
        if (principal instanceof OAuth2User oAuth2User) {
            username = oAuth2User.getAttribute("login");
            model.addAttribute("username", username);
            model.addAttribute("name", oAuth2User.getAttribute("name"));
            memberRegisterForm = new MemberRegisterForm();
            memberRegisterForm.setName(oAuth2User.getAttribute("name"));
            memberRegisterForm.setUsername(username);
        } else {
            throw new IllegalStateException("Not an OAuth2User");
        }
        List<Huddle> huddles = huddleService.allHuddles();
        List<HuddleSummaryView> huddleSummaryViews = HuddleSummaryView.from(huddles, username);
        model.addAttribute("register", memberRegisterForm);
        model.addAttribute("huddles", huddleSummaryViews);
        return "member-register";
    }

    @PostMapping("/member/register")
    public String register(MemberRegisterForm memberRegisterForm) {
        HuddleId huddleId = HuddleId.of(memberRegisterForm.getId());

        huddleService.registerParticipant(huddleId,
                                          memberRegisterForm.getName(),
                                          memberRegisterForm.getUsername(),
                                          "blankDiscordName");

        return "redirect:/member/register";
    }

}
