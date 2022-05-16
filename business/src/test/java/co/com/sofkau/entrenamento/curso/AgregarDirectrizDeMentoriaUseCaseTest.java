package co.com.sofkau.entrenamento.curso;

import co.com.sofka.business.generic.UseCaseHandler;
import co.com.sofka.business.repository.DomainEventRepository;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkau.entrenamiento.curso.Mentoria;
import co.com.sofkau.entrenamiento.curso.commands.AgregarDirectrizMentoria;
import co.com.sofkau.entrenamiento.curso.events.CursoCreado;
import co.com.sofkau.entrenamiento.curso.events.DirectrizAgregadaAMentoria;
import co.com.sofkau.entrenamiento.curso.events.MentoriaCreada;
import co.com.sofkau.entrenamiento.curso.values.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgregarDirectrizDeMentoriaUseCaseTest{

    @InjectMocks
    private AgregarDirectrizDeMentoriaUseCase useCase;

    @Mock
    private DomainEventRepository repository;

    @Test
    @DisplayName("Agregar Directriz")
    public void agregarDirectrizMentoria(){
        //Arrange
            CursoId cursoId = CursoId.of("jjjj");
            MentoriaId mentoriaId= MentoriaId.of("cccc");
            Directiz directiz = new Directiz("Vamos para playa");

            var command = new AgregarDirectrizMentoria(cursoId,mentoriaId,directiz);
            when(repository.getEventsBy("jjjj")).thenReturn(history());
            useCase.addRepository(repository);

            //Act
            var events = UseCaseHandler.getInstance().
                    setIdentifyExecutor(command.getCursoId().value()).
                    syncExecutor(useCase,new RequestCommand<>(command))
                    .orElseThrow().getDomainEvents();

            //Assert
        var event = (DirectrizAgregadaAMentoria) events.get(0);
        Assertions.assertEquals("Vamos para playa",event.getDirectiz().value());
    }

    private List<DomainEvent> history() {
        Nombre nombre = new Nombre("DDD");
        Descripcion descripcion = new Descripcion("DDD useCase");
        var event = new CursoCreado(
                nombre,
                descripcion
        );

        MentoriaId mentoriaId = MentoriaId.of("cccc");
        Nombre nombreMentoria = new Nombre("Creando caso de uso y test");
        Fecha fechaCreada = new Fecha(LocalDateTime.now(), LocalDate.now());
        var event2 = new MentoriaCreada(
                mentoriaId,
                nombreMentoria,
                fechaCreada

        );
        event.setAggregateRootId("xxxxx");
        return List.of(event, event2);
    }



}