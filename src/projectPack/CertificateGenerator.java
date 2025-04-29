package projectPack;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

    public class CertificateGenerator {
        /**
         *
         * @param userName
         * @param iqScore
         * @param timeTaken
         * @param famousPerson
         * @param savePath
         */
        public static void generateCertificate(String userName, int iqScore, String timeTaken, String famousPerson, String savePath) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);
                PDRectangle mediaBox = page.getMediaBox();
                float pageWidth = mediaBox.getWidth();
                float y = mediaBox.getHeight() - 100;

                //Load font
                File fontFile = new File("lib/fonts/times.ttf");
                PDType0Font font = PDType0Font.load(document, fontFile);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                //Add background image
                try {
                    PDImageXObject bgImage = PDImageXObject.createFromFile("images/icons/SmartHireLogo25.png", document);
                    contentStream.drawImage(bgImage, 0, 0, mediaBox.getWidth(), mediaBox.getHeight());
                } catch (IOException e) {
                    System.out.println("Background image not found or failed to load. Skipping.");
                }

                //Certificate title
                contentStream.beginText();
                contentStream.setFont(font, 32);
                contentStream.newLineAtOffset(centerText("Certificate of Achievement", font, 28, pageWidth), y);
                contentStream.showText("Certificate of Achievement");
                contentStream.endText();

                y -= 60;

                //Congrats message
                contentStream.beginText();
                contentStream.setFont(font, 22);
                contentStream.newLineAtOffset(centerText("Congratulations, " + userName + "!", font, 18, pageWidth), y);
                contentStream.showText("Congratulations, " + userName + "!");
                contentStream.endText();

                y -= 40;

                //Test score line
                contentStream.beginText();
                contentStream.setFont(font, 20);
                contentStream.newLineAtOffset(centerText("You completed the IQ test with an impressive score of  " + iqScore + ".", font, 16, pageWidth), y);
                contentStream.showText("You completed the IQ test with an impressive score of  " + iqScore + ".");
                contentStream.endText();

                y -= 30;

                //Time taken line
                contentStream.beginText();
                contentStream.setFont(font, 20);
                contentStream.newLineAtOffset(centerText("Time Taken: " + timeTaken, font, 16, pageWidth), y);
                contentStream.showText("Time Taken: " + timeTaken);
                contentStream.endText();

                y -= 30;

                //Famous person line
                contentStream.beginText();
                contentStream.setFont(font, 20);
                contentStream.newLineAtOffset(centerText("Your IQ is similar to: " + famousPerson, font, 20, pageWidth), y);
                contentStream.showText("Your IQ is similar to: " + famousPerson);
                contentStream.endText();

                y -= 50;

                //Date
                contentStream.beginText();
                contentStream.setFont(font, 20);
                contentStream.newLineAtOffset(centerText("Date: " + LocalDate.now(), font, 18, pageWidth), y);
                contentStream.showText("Date: " + LocalDate.now());
                contentStream.endText();

                contentStream.close();
                document.save(savePath);

                JOptionPane.showMessageDialog(null, "Certificate saved to: " + savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         *
         * @param text
         * @param font
         * @param fontSize
         * @param pageWidth
         * @return
         * @throws IOException
         */
        private static float centerText(String text, PDType0Font font, int fontSize, float pageWidth) throws IOException {
            float textWidth = font.getStringWidth(text) / 1000 * fontSize;
            return (pageWidth - textWidth) / 2;
        }
    }
