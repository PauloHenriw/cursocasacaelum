	package br.com.caelum.ingresso.validacao;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;
import junit.framework.Assert;

public class GerenciadorDeSessao {

	private List<Sessao> sessoesDaSala;
	
	public GerenciadorDeSessao(List<Sessao> sessoesDaSala) {
		this.sessoesDaSala = sessoesDaSala;
	}
	
	public boolean cabe(Sessao sessaoNova) {
		if (terminaAmanha(sessaoNova)) {
			return false;
		}
	
	return sessoesDaSala.stream().noneMatch
			(sessaoExistente ->	horarioIsConflitante(sessaoExistente, sessaoNova)
											);
	}
	
	private boolean terminaAmanha(Sessao sessao) {
		
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessao);
		LocalDateTime ultimoSegundoDeHoje = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		
		if(terminoSessaoNova.isAfter(ultimoSegundoDeHoje)) {
			return true;
		}
		return false;
	}
	
	private boolean horarioIsConflitante(Sessao sessaoExistente, Sessao sessaoNova) {
		
		LocalDateTime inicioSessaoExistente = getInicioSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime terminoSessaoExistente = getTerminoSessaoComDiaDeHoje(sessaoExistente);
		LocalDateTime inicioSessaoNova = getInicioSessaoComDiaDeHoje(sessaoNova);
		LocalDateTime terminoSessaoNova = getTerminoSessaoComDiaDeHoje(sessaoNova);
		
		boolean sessaoNovaTerminaAntesDaExistente = terminoSessaoNova.isBefore(inicioSessaoExistente);
		boolean sessaoNovaComecaAntesDaExistente = terminoSessaoExistente.isBefore(inicioSessaoNova);
		
		
		if(sessaoNovaTerminaAntesDaExistente || sessaoNovaComecaAntesDaExistente) {
			return false;
		}
			return true;
	}
	
		private LocalDateTime getInicioSessaoComDiaDeHoje(Sessao sessao) {
			LocalDate hoje = LocalDate.now();
			
			return sessao.getHorario().atDate(hoje);
		}
		
		private LocalDateTime getTerminoSessaoComDiaDeHoje(Sessao sessao) {
			LocalDateTime inicioSessaoNova = getInicioSessaoComDiaDeHoje(sessao);
			
			return inicioSessaoNova.plus(sessao.getFilme().getDuracao());
		}
		

	     @Test
	     public void garanteQueNaoDevePermitirSessaoNoMesmoHorario() {

	         Filme filme = new Filme("Rogue One", Duration.ofMinutes(120),  
	                         "SCI-FI", BigDecimal.ONE);
	         LocalTime horario = LocalTime.parse("10:00:00");

	         Sala sala = new Sala("Eldorado - IMAX", BigDecimal.ONE);
	         List<Sessao> sessoes = Arrays.asList(new Sessao(horario, filme, sala));

	         Sessao sessao = new Sessao(horario, filme, sala);

	         GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);

	         Assert.assertFalse(gerenciador.cabe(sessao));
	     }

	     @Test
	     public void garanteQueNaoDevePermitirSessoesTerminandoDentroDoHorarioDeUmaSessaoJaExistente()  
	     {
	         Filme filme = new Filme("Rogue One", Duration.ofMinutes(120),  
	                         "SCI-FI", BigDecimal.ONE);
	         LocalTime horario = LocalTime.parse("10:00:00");

	         Sala sala = new Sala("Eldorado - IMAX", BigDecimal.ONE);
	         List<Sessao> sessoes = Arrays.asList(new Sessao(horario, filme, sala));

	         Sessao sessao = new Sessao(horario.plusHours(1), filme, sala);
	         GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);

	         Assert.assertFalse(gerenciador.cabe(sessao));

	     }

	     @Test
	     public void garanteQueNaoDevePermitirSessoesIniciandoDentroDoHorarioDeUmaSessaoJaExistente()  
	     {
	         Filme filme = new Filme("Rogue One", Duration.ofMinutes(120),  
	                         "SCI-FI", BigDecimal.ONE);
	         LocalTime horario = LocalTime.parse("10:00:00");
	         Sala sala = new Sala("Eldorado - IMAX", BigDecimal.ONE);

	         List<Sessao> sessoesDaSala = Arrays.asList(new Sessao(horario, filme, sala));

	         GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoesDaSala);
	         Assert.assertFalse(gerenciador.cabe(new Sessao(horario.minusHours(1),  
	                                 filme, sala)));

	     }

	     @Test
	     public void garanteQueDevePermitirUmaInsercaoEntreDoisFilmes() {
	         Sala sala = new Sala("Eldorado - IMAX", BigDecimal.ONE);

	         Filme filme1 = new Filme("Rogue One", Duration.ofMinutes(120),  
	                         "SCI-FI", BigDecimal.ONE);
	         LocalTime dezHoras = LocalTime.parse("10:00:00");
	         Sessao sessaoDasDez = new Sessao(dezHoras, filme1, sala);

	         Filme filme2 = new Filme("Rogue One", Duration.ofMinutes(120),  
	                         "SCI-FI", BigDecimal.ONE);
	         LocalTime dezoitoHoras = LocalTime.parse("18:00:00");
	         Sessao sessaoDasDezoito = new Sessao(dezoitoHoras,  
	                                         filme2, sala);

	         List<Sessao> sessoes = Arrays.asList(sessaoDasDez, sessaoDasDezoito);

	         GerenciadorDeSessao gerenciador = new GerenciadorDeSessao(sessoes);

	         Assert.assertTrue(gerenciador.cabe(new Sessao(LocalTime.parse("13:00:00"),  
	                             filme2, sala)));
	     }
	 }