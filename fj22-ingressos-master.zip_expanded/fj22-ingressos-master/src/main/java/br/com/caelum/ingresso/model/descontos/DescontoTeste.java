package br.com.caelum.ingresso.model.descontos;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

import org.junit.Test;

import br.com.caelum.ingresso.model.Filme;
import br.com.caelum.ingresso.model.Ingresso;
import br.com.caelum.ingresso.model.Sala;
import br.com.caelum.ingresso.model.Sessao;
import junit.framework.Assert;

public class DescontoTeste {
	
	
	 @Test
	   public void deveConcederDescontoDe30PorcentoParaIngressosDeClientesDeBancos() {

	       Sala sala = new Sala("Eldorado - IMAX", new BigDecimal("20.5"));
	       Filme filme = new Filme("Rogue One", Duration.ofMinutes(120),
	                       "SCI-FI", new BigDecimal("12"));
	       Sessao sessao = new Sessao(LocalTime.parse("10:00:00"), filme, sala);
	       Ingresso ingresso = new Ingresso(sessao, new DescontoBanco());

	       BigDecimal precoOriginal = new BigDecimal("22.75");

	       Assert.assertEquals(precoOriginal, ingresso.getPreco());

	   }

	   @Test
	   public void deveConcederDescontoDe50PorcentoParaIngressoDeEstudante() {

	       Sala sala = new Sala("Eldorado - IMAX", new BigDecimal("20.5"));
	       Filme filme = new Filme("Rogue One", Duration.ofMinutes(120),
	                       "SCI-FI", new BigDecimal("12"));
	       Sessao sessao = new Sessao(LocalTime.parse("10:00:00"), filme, sala);
	       Ingresso ingresso = new Ingresso(sessao, new DescontoEstudante());

	       BigDecimal precoOriginal = new BigDecimal("16.25");

	       Assert.assertEquals(precoOriginal, ingresso.getPreco());

	   }

	   @Test
	   public void naoDeveConcederDescontoParaIngressoNormal() {

	       Sala sala = new Sala("Eldorado - IMAX", new BigDecimal("20.5"));
	       Filme filme = new Filme("Rogue One", Duration.ofMinutes(120),
	                       "SCI-FI", new BigDecimal("12"));
	       Sessao sessao = new Sessao(LocalTime.parse("10:00:00"), filme, sala);
	       Ingresso ingresso = new Ingresso(sessao, new SemDesconto());

	       BigDecimal precoOriginal = new BigDecimal("32.5");

	       Assert.assertEquals(precoOriginal, ingresso.getPreco());

	   }
	}
