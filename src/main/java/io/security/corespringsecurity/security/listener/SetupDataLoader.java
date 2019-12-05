package io.security.corespringsecurity.security.listener;

import io.anymobi.domain.dto.event.EventDto;
import io.anymobi.domain.entity.board.Board;
import io.anymobi.domain.entity.event.Event;
import io.anymobi.domain.entity.sec.*;
import io.anymobi.domain.entity.users.User;
import io.anymobi.repositories.jpa.board.BoardRepository;
import io.anymobi.repositories.jpa.event.EventRepository;
import io.anymobi.repositories.jpa.security.*;
import io.anymobi.repositories.jpa.users.UserRepository;
import io.anymobi.security.enums.SocialType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private RoleHierarchyRepository roleHierarchyRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AccessIpRepository accessIpRepository;

    @Autowired
    ModelMapper modelMapper;

    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();

        for(int i=1; i<=30; i++){
            EventDto.CreateOrUpdate eventDto = setupEventData(i);
            Event mapperEvent = modelMapper.map(eventDto, Event.class);
            eventRepository.save(mapperEvent);
        }

        setupAccessIpData();

        alreadySetup = true;
    }



    private void setupSecurityResources() {
        Set<Role> roles = new HashSet<>();
        Set<Groups> groups = new HashSet<>();

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        roles.add(adminRole);
        Groups adminGroups = createGroupsIfNotFound("관리자그룹", roles);
        groups.add(adminGroups);
        createResourceIfNotFound("/admin/**", "", roles, "url");
        User user = createUserIfNotFound("admin@gmail.com", "admin@admin.com", null, "adminFirst", "adminLast", "pass", roles, groups);
        
        Set<Role> roles1 = new HashSet<>();
        Set<Groups> groups1 = new HashSet<>();

        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");
        roles1.add(managerRole);
        Groups managerGroups = createGroupsIfNotFound("매니저그룹", roles1);
        groups1.add(managerGroups);
        createResourceIfNotFound("io.anymobi.test.method.MethodService.methodTest", "", roles1, "method");
        createResourceIfNotFound("io.anymobi.test.method.MethodService.innerCallMethodTest", "", roles1, "method");
        createResourceIfNotFound("execution(* io.anymobi.test.aop.*Service.*(..))", "", roles1, "pointcut");
        createUserIfNotFound("manager@gmail.com", "manager@gmail.com", null,"managerFirst", "managerLast", "pass", roles1, groups1);
        createGroupHierarchyIfNotFound(managerGroups, adminGroups);
        createRoleHierarchyIfNotFound(managerRole, adminRole);
        
        Set<Role> roles2 = new HashSet<>();
        Set<Groups> groups2 = new HashSet<>();

        Role directorRole = createRoleIfNotFound("ROLE_DIRECTOR", "디렉터");
        roles2.add(directorRole);
        Groups directorGroups = createGroupsIfNotFound("디렉터그룹", roles2);
        groups2.add(directorGroups);
        createResourceIfNotFound("/director/**", "", roles2, "url");
        createUserIfNotFound("director@gmail.com", "director@gmail.com", SocialType.GOOGLE,"userFirst1", "userLast1", "pass", roles2, groups2);
        createGroupHierarchyIfNotFound(directorGroups, adminGroups);
        createRoleHierarchyIfNotFound(directorRole, adminRole);
        
        Set<Role> roles3 = new HashSet<>();
        Set<Groups> groups3 = new HashSet<>();

        Role childRole1 = createRoleIfNotFound("ROLE_USER", "정회원");
        roles3.add(childRole1);
        Groups userGroups = createGroupsIfNotFound("정회원그룹", roles3);
        groups3.add(userGroups);
        createResourceIfNotFound("/users/**", "", roles3, "url");
        createUserIfNotFound("onjsdnjs@gmail.com", "onjsdnjs@gmail.com", SocialType.GOOGLE,"userFirst1", "userLast1", "pass", roles3, groups3);
        createGroupHierarchyIfNotFound(userGroups, managerGroups);
        createRoleHierarchyIfNotFound(childRole1, managerRole);
        
        Set<Role> roles4 = new HashSet<>();
        Set<Groups> groups4 = new HashSet<>();

        Role childRole3 = createRoleIfNotFound("ROLE_IUSER", "준회원");
        roles4.add(childRole3);
        Groups userGroups2 = createGroupsIfNotFound("준회원그룹", roles4);
        groups4.add(userGroups2);
        createResourceIfNotFound("/users/**", "", roles4, "url");
        createUserIfNotFound("onjsdnjs@daum.com", "onjsdnjs@daum.com", SocialType.GOOGLE,"userFirst1", "userLast1", "pass", roles4, groups4);
        createGroupHierarchyIfNotFound(userGroups2, managerGroups);
        createRoleHierarchyIfNotFound(childRole3, managerRole);
        
        Set<Role> roles5 = new HashSet<>();
        Set<Groups> groups5 = new HashSet<>();

        childRole3 = createRoleIfNotFound("ROLE_IUSER", "준회원");
        roles5.add(childRole3);
        userGroups2 = createGroupsIfNotFound("준회원그룹", roles5);
        groups5.add(userGroups2);
        createResourceIfNotFound("/users/**", "", roles5, "url");
        createUserIfNotFound("onjsdnjs@daum.com", "onjsdnjs@daum.com", SocialType.GOOGLE,"userFirst1", "userLast1", "pass", roles5, groups5);
        createGroupHierarchyIfNotFound(userGroups2, userGroups);
        createRoleHierarchyIfNotFound(childRole3, childRole1);
        Set<Role> roles6 = new HashSet<>();

        childRole1 = createRoleIfNotFound("ROLE_USER", "정회원");
        roles6.add(childRole1);
        userGroups = createGroupsIfNotFound("정회원그룹", roles6);
        createResourceIfNotFound("/board/**", "",roles6, "url");
        Set<Role> roles7 = new HashSet<>();

        childRole1 = createRoleIfNotFound("ROLE_USER", "정회원");
        roles7.add(childRole1);
        userGroups = createGroupsIfNotFound("정회원그룹", roles7);
        createResourceIfNotFound("/boards/**", "",roles7, "url");
        Set<Role> roles8 = new HashSet<>();

        Role anonymousRole = createRoleIfNotFound("ROLE_ANONYMOUS", "익명사용자");
        createRoleHierarchyIfNotFound(anonymousRole, childRole1);

        Role childRole2 = createRoleIfNotFound("ROLE_USER2", "정회원2");
        roles8.add(childRole2);
        User user2 = createUserIfNotFound("user2", "onjsdnjs@facebook.com", SocialType.FACEBOOK,"userFirst2", "userLast2", "pass", roles3, groups3);
        userGroups = createGroupsIfNotFound("정회원그룹", roles8);
        createResourceIfNotFound("/users/**", "",roles8, "url");
        Set<Role> roles9 = new HashSet<>();

        Role facebookRole = createRoleIfNotFound("ROLE_FACEBOOK", "페이스북사용자");
        roles9.add(facebookRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles9);
        createResourceIfNotFound("/facebook", "",roles9, "url");
        Set<Role> roles10 = new HashSet<>();

        facebookRole = createRoleIfNotFound("ROLE_FACEBOOK", "페이스북사용자");
        roles10.add(facebookRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles10);
        createResourceIfNotFound("/board/**", "",roles10, "url");
        Set<Role> roles11 = new HashSet<>();

        facebookRole = createRoleIfNotFound("ROLE_FACEBOOK", "페이스북사용자");
        roles11.add(facebookRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles11);
        createResourceIfNotFound("/boards/**", "",roles11, "url");
        Set<Role> roles12 = new HashSet<>();

        Role googleRole = createRoleIfNotFound("ROLE_GOOGLE", "구글사용자");
        roles12.add(googleRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles12);
        createResourceIfNotFound("/google", "",roles12, "url");
        Set<Role> roles13 = new HashSet<>();

        googleRole = createRoleIfNotFound("ROLE_GOOGLE", "구글사용자");
        roles13.add(googleRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles13);
        createResourceIfNotFound("/board/**", "",roles13, "url");
        Set<Role> roles14 = new HashSet<>();

        googleRole = createRoleIfNotFound("ROLE_GOOGLE", "구글사용자");
        roles14.add(googleRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles14);
        createResourceIfNotFound("/boards/**", "",roles14, "url");
        Set<Role> roles15 = new HashSet<>();

        Role kakaoRole = createRoleIfNotFound("ROLE_KAKAO", "카카오사용자");
        roles15.add(kakaoRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles15);
        createResourceIfNotFound("/kakao", "",roles15, "url");
        Set<Role> roles16 = new HashSet<>();

        kakaoRole = createRoleIfNotFound("ROLE_KAKAO", "카카오사용자");
        roles16.add(childRole1);
        userGroups = createGroupsIfNotFound("정회원그룹", roles16);
        createResourceIfNotFound("/board/**", "",roles16, "url");
        Set<Role> roles17 = new HashSet<>();

        kakaoRole = createRoleIfNotFound("ROLE_KAKAO", "카카오사용자");
        roles17.add(kakaoRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles17);
        createResourceIfNotFound("/boards/**", "",roles17, "url");
        Set<Role> roles18 = new HashSet<>();

        anonymousRole = createRoleIfNotFound("ROLE_ANONYMOUS", "익명사용자");
        roles18.add(anonymousRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles18);
        createResourceIfNotFound("/api/**", "GET",roles18, "url");
        Set<Role> roles19 = new HashSet<>();

        anonymousRole = createRoleIfNotFound("ROLE_ANONYMOUS", "익명사용자");
        roles19.add(anonymousRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles19);
        createResourceIfNotFound("/oauth/token", "",roles19, "url");
        Set<Role> roles20 = new HashSet<>();

        anonymousRole = createRoleIfNotFound("ROLE_ANONYMOUS", "익명사용자");
        roles20.add(anonymousRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles20);
        createResourceIfNotFound("/users/*regist*", "",roles20, "url");
        Set<Role> roles21 = new HashSet<>();

        anonymousRole = createRoleIfNotFound("ROLE_ANONYMOUS", "익명사용자");
        roles21.add(anonymousRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles21);
        createResourceIfNotFound("/users/successRegister*", "",roles21, "url");
        Set<Role> roles22 = new HashSet<>();

        anonymousRole = createRoleIfNotFound("ROLE_ANONYMOUS", "익명사용자");
        roles22.add(anonymousRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles22);
        createResourceIfNotFound("/users/loginRememberMe*", "",roles22, "url");
        Set<Role> roles23 = new HashSet<>();

        anonymousRole = createRoleIfNotFound("ROLE_ANONYMOUS", "익명사용자");
        roles23.add(anonymousRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles23);
        createResourceIfNotFound("/users/logout*", "",roles23, "url");
        Set<Role> roles24 = new HashSet<>();

        anonymousRole = createRoleIfNotFound("IP_LOCAL_HOST", "IP주소");
        roles24.add(anonymousRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles24);
        createResourceIfNotFound("/board/**", "",roles24, "url");
        Set<Role> roles25 = new HashSet<>();

        anonymousRole = createRoleIfNotFound("ROLE_ANONYMOUS", "익명사용자");
        roles25.add(anonymousRole);
        userGroups = createGroupsIfNotFound("정회원그룹", roles25);
        createResourceIfNotFound("/api/**", "POST", roles25, "url");
        Set<Role> roles26 = new HashSet<>();

        roles26.add(anonymousRole);
        createResourceIfNotFound("/api/**", "PUT", roles26, "url");

        User finalUser = user;
        IntStream.rangeClosed(1, 200).forEach(index ->
                boardRepository.save(Board.builder()
                        .title("게시글"+index)
                        .contents("컨텐츠")
                        .boardType("associate")
                        //.createUpdateDT(new CreateUpdateDT())
                        .user(finalUser).build())
        );
    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {

        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
        }
        return roleRepository.save(role);
    }

    @Transactional
    public User createUserIfNotFound(final String userName, final String email, SocialType socialType, final String firstName, final String lastName, final String password, Set<Role> roleSet, Set<Groups> groupSet) {

        User user = userRepository.findByUsername(userName);

        if (user == null) {
            user = User.builder()
                    .username(userName)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .socialType(socialType)
                    .isUsing2FA(false)
                    .password(passwordEncoder.encode(password))
                    .enabled(true)
                    .createdDate(LocalDateTime.now())
                    .userRoles(roleSet)
                    .groupUsers(groupSet)
                    .build();
        }
        return userRepository.save(user);
    }

    @Transactional
    public Resources createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {
            resources = Resources.builder()
                    .resourceName(resourceName)
                    .roleSet(roleSet)
                    .httpMethod(httpMethod)
                    .resourceType(resourceType)
                    .ordernum(count.incrementAndGet())
                    .build();
        }
        return resourcesRepository.save(resources);
    }

    @Transactional
    public Groups createGroupsIfNotFound(String groupName, Set<Role> roles) {
        Groups groups = groupsRepository.findByGroupName(groupName);

        if (groups == null) {
            groups = Groups.builder()
                    .groupName(groupName)
                    .groupRoles(roles)
                    .build();
        }
        return groupsRepository.save(groups);
    }

    @Transactional
    public void createGroupHierarchyIfNotFound(Groups childGroup, Groups parentGroup) {

        Groups groups = groupsRepository.findByGroupName(parentGroup.getGroupName());
        if (groups == null) {
            groups = Groups.builder()
                    .groupName(parentGroup.getGroupName())
                    .build();
        }
        Groups pGroup = groupsRepository.save(groups);

        groups = groupsRepository.findByGroupName(childGroup.getGroupName());
        if (groups == null) {
            groups = Groups.builder()
                    .groupName(childGroup.getGroupName())
                    .build();
        }

        Groups cGroup = groupsRepository.save(groups);
        cGroup.setParentGroups(pGroup);
    }

    @Transactional
    public void createRoleHierarchyIfNotFound(Role childRole, Role parentRole) {

        RoleHierarchy roleHierarchy = roleHierarchyRepository.findByChildName(parentRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(parentRole.getRoleName())
                    .build();
        }
        RoleHierarchy parentRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);

        roleHierarchy = roleHierarchyRepository.findByChildName(childRole.getRoleName());
        if (roleHierarchy == null) {
            roleHierarchy = RoleHierarchy.builder()
                    .childName(childRole.getRoleName())
                    .build();
        }

        RoleHierarchy childRoleHierarchy = roleHierarchyRepository.save(roleHierarchy);
        childRoleHierarchy.setParentName(parentRoleHierarchy);
    }

    private EventDto.CreateOrUpdate setupEventData(int index) {

        return EventDto.CreateOrUpdate.builder()
                .name("test event" + index)
                .description("testing event apis" + index)
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 10, 15, 0, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 3, 23, 59))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 10, 9, 0))
                .endEventDateTime(LocalDateTime.of(2018, 11, 10, 14, 0))
                .location("anymobi" + index)
                .basePrice(50000 + index)
                .maxPrice(10000 + index)
                .build();
    }

    private void setupAccessIpData() {
        AccessIp byIpAddress = accessIpRepository.findByIpAddress("127.0.0.1");
        if (byIpAddress == null) {
        AccessIp accessIp = AccessIp.builder()
                    .ipAddress("127.0.0.1")
                    .build();
        accessIpRepository.save(accessIp);
        }

    }
}