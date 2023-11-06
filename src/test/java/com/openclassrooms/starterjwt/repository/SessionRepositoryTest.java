package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SessionRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void SessionRepositorySaveOneSession() {
        Teacher teacher = new Teacher();
        teacher.setLastName("Dupont");
        teacher.setFirstName("Jean");
        // Save the teacher to the database
        testEntityManager.persist(teacher);

        Session session = new Session();
        session.setName("Anglais");
        session.setDate(new Date());
        session.setDescription("cours d'anglais niveau 1 développement");
        session.setTeacher(teacher);

        // Save the session
        Session savedSession = sessionRepository.save(session);
        testEntityManager.flush();

        // Find the saved session by id in the database
        Session foundSession = testEntityManager.find(Session.class, savedSession.getId());
        System.out.println("sessions créer"+session);

        // Check that the found session is not null and has the expected name
        Assertions.assertThat(foundSession).isNotNull();
        Assertions.assertThat(foundSession.getName()).isEqualTo("Anglais");
        Assertions.assertThat(foundSession.getDescription()).isEqualTo("cours d'anglais niveau 1 développement");
        Assertions.assertThat(foundSession.getTeacher()).isEqualTo(teacher);
    }

    @Test
    public void SessionRepositoryGetAllSessions() {
        Teacher teacher = new Teacher();
        teacher.setLastName("Chris");
        teacher.setFirstName("Topher");
        // Save the teacher
        testEntityManager.persist(teacher);

        Session session = new Session();
        session.setName("Math");
        session.setDate(new Date());
        session.setDescription("cours de math niveau 1 science");
        session.setTeacher(teacher);
        // Save the teacher session
        testEntityManager.persist(session);

        Teacher teacher2 = new Teacher();
        teacher2.setLastName("Albert");
        teacher2.setFirstName("Einstein");
        // Save the teacher
        testEntityManager.persist(teacher);

        Session session2 = new Session();
        session2.setName("Physics");
        session2.setDate(new Date());
        session2.setDescription("cours de physique niveau 1 science");
        session2.setTeacher(teacher);
        // Save the teacher session
        testEntityManager.persist(session2);

        testEntityManager.flush();

        List<Session> sessionList = sessionRepository.findAll();
            System.out.println("Liste des sessions"+sessionList);


        Assertions.assertThat(sessionList).isNotNull();
        Assertions.assertThat(sessionList.size()).isEqualTo(2);
    }
    @Test
    public void SessionRepositoryGetSessionsById() {
        // Create and persist a teacher
        Teacher teacher = new Teacher();
        teacher.setLastName("Bertholt");
        teacher.setFirstName("Titan");
        testEntityManager.persist(teacher);

        // Create and persist a session
        Session session = new Session();
        session.setName("Combat");
        session.setDate(new Date());
        session.setDescription("cours de combat niveau 1");
        session.setTeacher(teacher);
        Session savedSession = testEntityManager.persistAndFlush(session);

        // Retrieve the session by id
        Optional<Session> foundSessionOpt = sessionRepository.findById(savedSession.getId());

        // Check that the session was found
        Assertions.assertThat(foundSessionOpt).isPresent();
        System.out.println("Liste des sessions"+foundSessionOpt);

        // Check that the found session has the expected name
        Session foundSession = foundSessionOpt.get();
        Assertions.assertThat(foundSession.getName()).isEqualTo("Combat");
        Assertions.assertThat(foundSession.getDescription()).isEqualTo("cours de combat niveau 1");
        Assertions.assertThat(foundSession.getTeacher()).isEqualTo(teacher);
    }

    @Test
    public void SessionRepositoryUpdateSession() {
        Teacher teacher= Teacher.builder()
                .firstName("Philippe")
                .lastName("Bouvard").build();


        Session session = Session.builder()
                .name("Humour")
                .date(new Date())
                .description("cour d'humour")
                .teacher(teacher).build();
        sessionRepository.save(session);

        Session updateSession = sessionRepository.findById(session.getId()).get();
        updateSession.setDescription("cour d'art dramatique");
        System.out.println("sessions mis à jour"+updateSession);

        Assertions.assertThat(updateSession.getDescription()).isNotNull();

    }

    @Test
    public void SessionRepositoryDeleteSession() {
        Teacher teacher= Teacher.builder()
                .firstName("Philippe")
                .lastName("Bouvard").build();


        Session session = Session.builder()
                .name("Humour")
                .date(new Date())
                .description("cour d'humour")
                .teacher(teacher).build();
        sessionRepository.save(session);
        System.out.println("sessions avant suppression"+session);

        sessionRepository.deleteById(session.getId());
        Optional<Session> sessionReturn = sessionRepository.findById(session.getId());
        Assertions.assertThat(sessionReturn).isEmpty();
        System.out.println("sessions après suppression"+ sessionReturn);

    }
}
