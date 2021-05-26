package com.scheduler.parser;

import com.scheduler.auth.Credential;
import com.scheduler.auth.CredentialRepository;
import com.scheduler.auth.Role;
import com.scheduler.auth.RoleRepository;
import com.scheduler.group.Group;
import com.scheduler.group.GroupRepository;
import com.scheduler.group.subgroup.SubGroup;
import com.scheduler.group.subgroup.SubGroupRepository;
import com.scheduler.parser.temporary.StudentTmp;
import com.scheduler.student.StudentRepository;
import com.scheduler.student.entity.Student;
import com.scheduler.university.University;
import com.scheduler.university.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocStoreService
{
    private final StudentRepository studentRepository;
    private final CredentialRepository credentialRepository;
    private final SubGroupRepository subGroupRepository;
    private final GroupRepository groupRepository;
    private final UniversityRepository universityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private University university;

    public void store( List<StudentTmp> studentTmps )
    {
        createAndStoreUniversity();
        Role role = createAndStoreStudentRole();
        studentTmps.forEach( studentTmp -> {
            Group group = groupRepository.findByName( studentTmp.getGroupName() );
            Student student = new Student();
            Credential credential = new Credential();

            credential.setLogin( studentTmp.getEmail() );
            credential.setPassword( encoder.encode( studentTmp.getPassword() ) );
            credential.setName( studentTmp.getName() );
            credential.setUniversityId( university.getId() );
            credential.setPasswordTemp( true );
            credential.setRole( role );
            credential = credentialRepository.save( credential );

            student.setName( studentTmp.getName() );
            student.setUniversityId( university.getId() );
            SubGroup subGroup = subGroupRepository.findByGroupAndNumber( group, Long.valueOf( studentTmp.getSubGroupNumber() ) );
            student.setSubGroup( subGroup );
            student.setSex( "M" );
            student.setCapitan( false );
            student.setCredential( credential );
            studentRepository.save( student );
        } );
    }

    private void createAndStoreUniversity()
    {
        University university = universityRepository.findByName( "БрГТУ" );//todo change
        if( university == null )
        {
            university = new University();
            university.setName( "БрГТУ" );
            university.setAddress( "ул.Московская 267" );
            university = universityRepository.save( university );
        }
        this.university = university;
    }

    private Role createAndStoreStudentRole()
    {
        Role role = roleRepository.findByName( "STUDENT" );
        if( role == null )
        {
            role = new Role();
            role.setName( "STUDENT" );
            role.setUniversityId( university.getId() );
            role = roleRepository.save( role );
        }
        return role;
    }

}
