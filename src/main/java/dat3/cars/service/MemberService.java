package dat3.cars.service;

import dat3.cars.dto.MemberRequest;
import dat3.cars.dto.MemberResponse;
import dat3.cars.entity.Member;
import dat3.cars.repositories.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
  MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public MemberResponse addMember(MemberRequest memberRequest){
    if(memberRepository.existsById(memberRequest.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this ID already exist");
    }
    if(memberRepository.existsByEmail(memberRequest.getEmail())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this Email already exist");
    }

    Member newMember = MemberRequest.getMemberEntity(memberRequest);
    newMember = memberRepository.save(newMember);
    return new MemberResponse(newMember, false); //False since anonymous uses can create "themself"
  }

  public List<MemberResponse> getMembers(boolean includeAll) {
    List<Member> members = memberRepository.findAll();
//    Please Never do it like this, at least not at the exam
//    List<MemberResponse> responses = new ArrayList<>();
//    for(Member m: members){
//      MemberResponse mr = new MemberResponse(m,includeAll);
//      responses.add(mr);
//    }
    List<MemberResponse> responses = members.stream().map(member -> new MemberResponse(member,includeAll)).toList();
    return responses;
  }

  public MemberResponse findMemberByUsername(String username, boolean includeAll) {
    Member found = memberRepository.findById(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
    return new MemberResponse(found,includeAll);
  }

  public ResponseEntity<Boolean> editMember(MemberRequest body, String username){
    Member memberToEdit = memberRepository.findById(username).orElseThrow(() ->
       new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this ID does not exist"));

    //Member can not change his username (primary key), ranking, approved and timestamps
    memberToEdit.setEmail(body.getEmail());
    memberToEdit.setPassword(body.getPassword());
    memberToEdit.setFirstName(body.getFirstName());
    memberToEdit.setLastName(body.getLastName());
    memberToEdit.setStreet(body.getStreet());
    memberToEdit.setZip(body.getZip());
    memberToEdit.setCity(body.getCity());
    memberRepository.save(memberToEdit);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  // ALTERNATIVE suggested by Mark Dyrby, who got the suggestion from "you know who"
  // This is the same as above, but with Optional
  // If you don't understand why this  is better, please ask "you know who"
  public ResponseEntity<Boolean> editMemberV2(MemberRequest body, String username){
    Member memberToEdit = memberRepository.findById(username).orElseThrow(() ->
       new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this ID does not exist"));

    Optional.ofNullable(body.getEmail()).ifPresent(memberToEdit::setEmail);
    Optional.ofNullable(body.getPassword()).ifPresent(memberToEdit::setPassword);
    Optional.ofNullable(body.getFirstName()).ifPresent(memberToEdit::setFirstName);
    Optional.ofNullable(body.getLastName()).ifPresent(memberToEdit::setLastName);
    Optional.ofNullable(body.getStreet()).ifPresent(memberToEdit::setStreet);
    Optional.ofNullable(body.getZip()).ifPresent(memberToEdit::setZip);
    Optional.ofNullable(body.getCity()).ifPresent(memberToEdit::setCity);
    memberRepository.save(memberToEdit);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  public ResponseEntity<Boolean> setRankingForUser(String username, int ranking){
    Member memberToEdit = memberRepository.findById(username).orElseThrow(() ->
       new ResponseStatusException(HttpStatus.BAD_REQUEST,"Member with this ID does not exist"));
    memberToEdit.setRanking(ranking);
    memberRepository.save(memberToEdit);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  public ResponseEntity<Boolean> deleteMemberByUsername(String username) {
    try {
      if (memberRepository.existsById(username)) {
        memberRepository.deleteById(username);
        return new ResponseEntity<>(true, HttpStatus.OK);
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not delete member, he is probably 'Active'");
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not delete member, he's probably 'Active'");
    }
  }
}
