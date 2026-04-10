package br.com.boleiroOn.domain.arrematacao.service;

import br.com.boleiroOn.domain.arrematacao.entity.ArrematacaoEntity;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PdfGeradorService {

    private final SpringTemplateEngine templateEngine;

    public byte[] gerarAutoArrematacaoPdf(ArrematacaoEntity arrematacao, String fotoBase64, String assinaturaBase64) throws Exception {

        Context context = new Context();

        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        String dataFormatada = arrematacao.getDataArrematacao() != null
                ? arrematacao.getDataArrematacao().format(formatadorData)
                : "";
        context.setVariable("dataArrematacao", dataFormatada);

        context.setVariable("condicional", arrematacao.isVendaCondicional() ? "Sim" : "Não");

        context.setVariable("valorArremate", arrematacao.getValorArrematacao() != null
                ? formatadorMoeda.format(arrematacao.getValorArrematacao())
                : "R$ 0,00");

        context.setVariable("valorComissao", arrematacao.getValorComissao() != null
                ? formatadorMoeda.format(arrematacao.getValorComissao())
                : "R$ 0,00");

        if (arrematacao.getLote() != null) {
            context.setVariable("numeroLote", arrematacao.getLote().getId());
        }

        if (arrematacao.getArrematante() != null) {
            var arrematante = arrematacao.getArrematante();

            if (arrematante.getLeilao() != null) {
                context.setVariable("nomeLeilao", arrematante.getLeilao().getNome());
            } else {
                context.setVariable("nomeLeilao", "Não Informado");
            }

            context.setVariable("nomeArrematante", arrematante.getNome());
            context.setVariable("placa", arrematante.getPlaca());
            context.setVariable("telefone", arrematante.getTelefone());
            context.setVariable("celular", arrematante.getCelular());
            context.setVariable("email", arrematante.getEmail());

            String enderecoFormatado = String.format("%s - %s/%s, CEP: %s",
                    arrematante.getEndereco() != null ? arrematante.getEndereco() : "",
                    arrematante.getCidade() != null ? arrematante.getCidade() : "",
                    arrematante.getUf() != null ? arrematante.getUf() : "",
                    arrematante.getCep() != null ? arrematante.getCep() : "");

            if (enderecoFormatado.trim().equals("- /, CEP:")) {
                enderecoFormatado = "Não Informado";
            }

            context.setVariable("endereco", enderecoFormatado);

        } else {
            context.setVariable("nomeLeilao", "Não Informado");
            context.setVariable("nomeArrematante", "Não Informado");
            context.setVariable("placa", "-");
            context.setVariable("telefone", "-");
            context.setVariable("celular", "-");
            context.setVariable("email", "-");
            context.setVariable("endereco", "Não Informado");
        }

        context.setVariable("fotoBase64", fotoBase64);
        context.setVariable("assinaturaBase64", assinaturaBase64);

        String htmlProcessado = templateEngine.process("auto-arrematacao", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlProcessado, "");
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        }
    }
}