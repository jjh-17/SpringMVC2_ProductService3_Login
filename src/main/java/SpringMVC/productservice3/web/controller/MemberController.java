package SpringMVC.productservice3.web.controller;

import SpringMVC.productservice3.domain.member.Member;
import SpringMVC.productservice3.domain.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final MemberRepository memberRepository;

    //회원 가입 폼 호출
    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) {
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute("member") Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors {}", bindingResult);
            return "members/addMemberForm";
        }

        memberRepository.save(member);
        log.info("save {}", member);
        return "redirect:/";
    }
}
