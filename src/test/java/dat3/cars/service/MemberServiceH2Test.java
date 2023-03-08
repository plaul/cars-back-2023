package dat3.cars.service;

import dat3.cars.dto.MemberRequest;
import dat3.cars.dto.MemberResponse;
import dat3.cars.entity.Member;
import dat3.cars.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberServiceH2Test {
  @Autowired
  public MemberRepository memberRepository;

  //See setup method below for why this is needed
  @Autowired
  private TestEntityManager entityManager;

  MemberService memberService;

  static Member m1;
  static Member m2;

  boolean dataIsInitialized = false;

  @BeforeEach
  void setUp() throws InterruptedException {
    if (dataIsInitialized) return;
    m1 = new Member("m1", "pw", "m1@a.dk", "bb", "jjj", "xx vej 34", "Lyngby", "2800");
    m2 = new Member("m2", "pw", "m2@a.dk", "aa", "lll", "xx vej 34", "Lyngby", "2800");
    m1 = memberRepository.saveAndFlush(m1);//Flush ensures date fields are set
    m2 = memberRepository.saveAndFlush(m2);

    //This is necessary to get the created dates from the DB and make m1 + m2 un-managed
    //entityManager.flush();
    entityManager.clear(); //Causes all managed entities to become detached.

    memberService = new MemberService(memberRepository); //Real DB is mocked away with H2
    dataIsInitialized = true;
  }

  @Test
  void addMember() {
    MemberRequest mr = MemberRequest.builder().
            username("m3").password("pw").email("h@a.dk").firstName("bb").lastName("cc").street("x vej 1").city("Lyngby").zip("2800").build();
    MemberResponse res = memberService.addMember(mr);
    assertEquals("m3", res.getUsername());
    assertEquals("h@a.dk", res.getEmail());
  }

  @Test
  void addMemberThrowsWithExistingUsername() {
    MemberRequest mr = MemberRequest.builder().
            username("m1").password("pw").email("h@a.dk").firstName("bb").lastName("cc").street("x vej 1").city("Lyngby").zip("2800").build();
    assertThrows(ResponseStatusException.class, () -> memberService.addMember(mr));

  }

  @Test
  void getMembersAdmin() {
    List<MemberResponse> members = memberService.getMembers(true);
    assertEquals(2, members.size());
    assertNotNull(members.get(0).getCreated());
    assertNotNull(members.get(0).getRanking());
  }

  @Test
  void getMembersNotAdmin() {
    List<MemberResponse> members = memberService.getMembers(false);
    assertEquals(2, members.size());
    assertNull(members.get(0).getCreated());
    assertNull(members.get(0).getRanking());
  }


  @Test
  void findMemberByUsername() {
    MemberResponse res = memberService.findMemberByUsername("m1",false);
    assertEquals("m1", res.getUsername());
    assertNull(res.getRanking());
    assertNull(res.getCreated());
  }

  @Test
  void editMember() throws InterruptedException {
    MemberRequest request = new MemberRequest(m1);
    //Fields to edit
    request.setStreet("New Street");
    request.setEmail("newEmail@a.dk");

    ResponseEntity<Boolean> res = memberService.editMember(request, m1.getUsername());
    assertTrue(res.getBody().booleanValue());

    readEditedMemberAndVerifyAllValues();
  }



  @Test
  void editMemberV2() {
    MemberRequest request = MemberRequest.builder().username(m1.getUsername()).build();
    //Fields to edit  --> Observe many fields in request this are null (not set)
    request.setStreet("New Street");
    request.setEmail("newEmail@a.dk");

    ResponseEntity<Boolean> res = memberService.editMemberV2(request, m1.getUsername());
    assertTrue(res.getBody().booleanValue());
    //Verify that the fields given above where changed, and all other fields are unchanged
    readEditedMemberAndVerifyAllValues();
  }

  private void readEditedMemberAndVerifyAllValues() {
//    Next two lines are necessary to make sure the date fields in the DB is actually updated
//    If this courses problems, SKIP date fields in the test and test via Postman
    memberRepository.flush(); //necessary to update dates

    Member editedMember = memberRepository.findById(m1.getUsername()).get();
    assertEquals(m1.getUsername(),editedMember.getUsername());
    assertEquals(m1.getRanking(),editedMember.getRanking());
    assertEquals("New Street",editedMember.getStreet());
    assertEquals("newEmail@a.dk",editedMember.getEmail());
    assertEquals(m1.getFirstName(),editedMember.getFirstName());
    assertEquals(m1.getLastName(),editedMember.getLastName());
    assertEquals(m1.getCity(),editedMember.getCity());
    assertEquals(m1.getZip(),editedMember.getZip());
    assertEquals(m1.getPassword(),editedMember.getPassword());

    assertTrue( editedMember.getLastEdited().isAfter(m1.getCreated()));
  }

  @Test
  void setRankingForUser() {
    memberService.setRankingForUser("m1", 77);
    MemberResponse res = memberService.findMemberByUsername("m1",true);
    assertEquals(77, res.getRanking());
  }

  @Test
  void deleteMemberByUsername() {
    memberService.deleteMemberByUsername("m1");
    List<MemberResponse> members = memberService.getMembers(true);
    assertEquals(1, members.size());
  }
}