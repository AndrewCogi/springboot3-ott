package com.cloudsoft.ott.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailSender {

	@Autowired
	private JavaMailSender mailSender;

	public void sendVerificationEmail(String toEmail, String url) {
		try {
			// MimeMessage 생성
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			String subject = "[Dokyo - MetroPath] 이메일 인증을 완료해주세요.";
            String content = "<!DOCTYPE html>" +
                             "<html lang=\"ko\">" +
                             "<head>" +
                             "    <meta charset=\"UTF-8\">" +
                             "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                             "    <title>[Dokyo - MetroPath] 이메일 인증</title>" +
                             "</head>" +
                             "<body style=\"font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f4f4f4;\">" +
                             "    <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #ddd; border-radius: 8px;\">" +
                             "        <tr>" +
                             "            <td style=\"background-color: #4CAF50; color: white; text-align: center; padding: 20px; border-radius: 8px 8px 0 0;\">" +
                             "                <h1 style=\"margin: 0; font-size: 20px;\">[Dokyo - MetroPath] 이메일 인증</h1>" +
                             "            </td>" +
                             "        </tr>" +
                             "        <tr>" +
                             "            <td style=\"padding: 20px; text-align: center;\">" +
                             "                <p style=\"margin: 10px 0; font-size: 16px; color: #333;\"><b>[Dokyo - MetroPath] 인증 이메일</b>입니다. \n아래 버튼을 클릭하여 이메일 인증을 완료해주세요.</p>" +
                             "                <a href=\"" + url + "\" style=\"display: inline-block; margin: 20px 0; padding: 15px 25px; background-color: #4CAF50; color: white; text-decoration: none; font-size: 16px; border-radius: 5px;\">이메일 인증하기</a>" +
                             "                <p style=\"margin: 10px 0; font-size: 16px; color: #333;\"><b>버튼이 동작하지 않을 경우</b> 아래 URL을 클릭해 인증을 진행해 주세요.</p>" +
                             "                <div style=\"margin: 20px 0; padding: 10px; background-color: #f9f9f9; border: 1px solid #ccc; border-radius: 5px; word-break: break-all;\">" +
                             "                    <a href=\"" + url + "\" style=\"text-decoration: none; color: #007BFF;\">" + url + "</a>" +
                             "                </div>" +
							 "                <p style=\"margin-top: 30px; font-size: 14px; color: #777;\">" +
                             "                    해당 인증 링크는 <strong>5분</strong> 동안 유효합니다.<br>" +
                             "                    본인이 요청하지 않았다면, 이 메일을 무시해 주세요.<br>" +
                             "                    문의사항은 <a href=\"mailto:cho990326@naver.com\">여기</a>로 문의해 주세요." +
                             "                </p>" +
                             "            </td>" +
                             "        </tr>" +
                             "        <tr>" +
                             "            <td style=\"text-align: center; padding: 10px; font-size: 12px; color: #999; border-top: 1px solid #ddd;\">" +
                             "                © Dokyo - MetroPath" +
                             "            </td>" +
                             "        </tr>" +
                             "    </table>" +
                             "</body>" +
                             "</html>";

			// 이메일 설정
			helper.setFrom("sgm3takoyaki@gmail.com", "Dokyo - MetroPath");
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(content, true); // HTML 내용

			// 메일 전송
			mailSender.send(mimeMessage);
			System.out.println("메일 전송 완료! -> [" + toEmail + "]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
