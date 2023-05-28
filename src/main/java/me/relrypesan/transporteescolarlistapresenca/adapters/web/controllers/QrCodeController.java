package me.relrypesan.transporteescolarlistapresenca.adapters.web.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.AlunoUseCase;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/qrcode")
public class QrCodeController {

    private final AlunoUseCase alunoUseCase;

    @GetMapping(value = "/download/{id_aluno}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<ByteArrayResource> downloadTelpFile(@PathVariable("id_aluno") String idAluno) throws WriterException, DocumentException, IOException {
        // Criar o documento PDF
        Document document = new Document();

        Aluno aluno = alunoUseCase.consultarAluno(idAluno);

        // Criar um ByteArrayOutputStream para armazenar o conteúdo do PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Criar um PdfWriter para escrever o documento no ByteArrayOutputStream
        PdfWriter.getInstance(document, outputStream);

        // Abrir o documento
        document.open();

        // Criar uma String para armazenar o conteúdo do PDF
        String pdfContent = "Nome: " + aluno.getNome() + "\n\n" +
                "Escola: " + aluno.getEscola().getNome() + "\n\n";

        // Adicionar o conteúdo do PDF ao documento
        document.add(new Paragraph(pdfContent));

        // Gerar o QR Code com a identificação
        String qrCodeText = "telp://aluno?id=" + aluno.getId();
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrCodeText, BarcodeFormat.QR_CODE, 250, 250);

        // Converter o BitMatrix em um array de bytes PNG usando o ZXing
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] qrCodeBytes = pngOutputStream.toByteArray();

        // Adicionar o QR Code ao documento
        com.lowagie.text.Image qrCodeImage = com.lowagie.text.Image.getInstance(qrCodeBytes);
        document.add(qrCodeImage);

        // Fechar o documento
        document.close();

        // Obter o conteúdo do PDF do ByteArrayOutputStream
        byte[] pdfBytes = outputStream.toByteArray();

        // Criar uma resposta HTTP com o PDF anexado
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        HttpHeaders headers = new HttpHeaders();
        String nomeSemEspaco = aluno.getNome().replace(' ', '-');
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ALUNO-" + nomeSemEspaco + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping(value = "/gerar_imagem/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCodeImage(@PathVariable("id") String idAluno,
                                                      @NotBlank @RequestParam(value = "size") Integer size) throws WriterException, IOException {

        if (size == null || size > 1000 || size < 25) {
            throw new BusinessException("Não é possivel gerar um QR Code menor que 25 e maior que 1000 de tamanho");
        }

        Aluno aluno = alunoUseCase.consultarAluno(idAluno);
        // Gerar o QR Code com a identificação fornecida
        String qrCodeText = "telp://aluno?id=" + aluno.getId();
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrCodeText, BarcodeFormat.QR_CODE, size, size);

        // Converter o BitMatrix em um array de bytes PNG usando o ZXing
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] qrCodeBytes = pngOutputStream.toByteArray();

        // Criar uma resposta HTTP com o QR Code como imagem
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qrcode.png");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(qrCodeBytes.length)
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeBytes);
    }

}
