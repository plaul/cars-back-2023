package dat3.cars.api;

import dat3.cars.dto.MemberRequest;
import dat3.cars.dto.MemberResponse;
import dat3.cars.service.MemberService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/members")
@RestController
@CrossOrigin
public class MemberController {

  MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  //Security ADMIN ONLY
  @GetMapping
  List<MemberResponse> getMembers(){
    return memberService.getMembers(true); //True --> Return all, since this is ADMIN only
  }

  //Security ADMIN ONLY
  @GetMapping(path = "/{username}")
  MemberResponse getMemberById(@PathVariable String username) {
    return memberService.findMemberByUsername(username, true);
  }

  //Security ANONYMOUS
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  MemberResponse addMember(@RequestBody MemberRequest body){
    return memberService.addMember(body);
  }

  //Security ADMIN  (eventually we will change it to use the currently logged in user)
  @PutMapping("/{username}")
  ResponseEntity<Boolean> editMember(@RequestBody MemberRequest body, @PathVariable String username){
    return memberService.editMember(body, username);
  }

  //Security ADMIN  (eventually we will change it to use the currently logged in user)
  @PutMapping("/v2/{username}")
  ResponseEntity<Boolean> editMemberV2(@RequestBody MemberRequest body, @PathVariable String username){
    return memberService.editMemberV2(body, username);
  }

  //Security ADMIN ONLY
  @PatchMapping("/ranking/{username}/{value}")
  void setRankingForUser(@PathVariable String username, @PathVariable int value) {
    memberService.setRankingForUser(username, value);
  }

  // Security ADMIN ONLY
  @DeleteMapping("/{username}")
  ResponseEntity<Boolean> deleteMemberByUsername(@PathVariable String username) {
    return memberService.deleteMemberByUsername(username);
  }

}
