package com.example.demo.common.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.common.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	private JavaMailSender mailSender;

	public MessageServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendOtpEmail(String toEmail, String subject, String otpCode) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(toEmail);
			helper.setSubject(subject);

			String htmlContent = String.format(
					"""
							<!DOCTYPE html>
							<html lang="vi">
							<head>
							    <meta charset="UTF-8">
							    <meta name="viewport" content="width=device-width, initial-scale=1.0">
							    <title>Mã OTP</title>
							</head>
							<body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
							    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; min-height: 100vh;">
							        <tr>
							            <td align="center" style="padding: 40px 20px;">
							                <table width="500" cellpadding="0" cellspacing="0" style="background-color: white; border-radius: 16px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">

							                    <!-- Header -->
							                    <tr>
							                        <td style="padding: 40px 40px 20px; text-align: center;">
							                            <div style="width: 80px; height: 80px; background-color: #667eea; border-radius: 50%%; margin: 0 auto 20px; text-align: center; line-height: 80px;">
							                                <span style="color: white; font-size: 32px;">🔐</span>
							                            </div>
							                            <h1 style="color: #2c3e50; margin: 0; font-size: 28px; font-weight: 600;">Xác thực OTP</h1>
							                            <p style="color: #7f8c8d; margin: 10px 0 0; font-size: 16px;">Mã xác thực của bạn</p>
							                        </td>
							                    </tr>

							                    <!-- OTP Code -->
							                    <tr>
							                        <td style="padding: 0 40px 20px;">
							                            <div style="background-color: #f8f9fa; border-radius: 12px; padding: 30px; text-align: center; border: 2px dashed #dee2e6;">
							                                <p style="color: #495057; margin: 0 0 15px; font-size: 18px; font-weight: 500;">Mã OTP của bạn là:</p>
							                                <div style="background-color: white; border-radius: 8px; padding: 20px; border: 2px solid #e63946; display: inline-block;">
							                                    <span style="font-size: 32px; font-weight: bold; color: #e63946; letter-spacing: 8px; font-family: 'Courier New', monospace;">%s</span>
							                                </div>
							                            </div>
							                        </td>
							                    </tr>
							                    <tr>
							                        <td style="padding: 0 40px 20px;">
							                            <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 8px; padding: 20px;">
							                                <div style="margin-bottom: 10px;">
							                                    <span style="color: #856404; font-size: 20px; margin-right: 10px;">⚠️</span>
							                                    <strong style="color: #856404; font-size: 16px;">Lưu ý quan trọng:</strong>
							                                </div>
							                                <ul style="color: #856404; margin: 0; padding-left: 20px; font-size: 14px; line-height: 1.6;">
							                                    <li>Mã OTP có hiệu lực trong <strong>5 phút</strong></li>
							                                    <li>Không chia sẻ mã này với bất kỳ ai</li>
							                                    <li>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email</li>
							                                </ul>
							                            </div>
							                        </td>
							                    </tr>
							                    <tr>
							                        <td style="padding: 20px 40px 40px; border-top: 1px solid #e9ecef;">
							                            <p style="color: #6c757d; margin: 0; font-size: 14px; line-height: 1.5; text-align: center;">
							                                Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!<br>
							                                <span style="color: #adb5bd; font-size: 12px;">Email này được gửi tự động, vui lòng không trả lời.</span>
							                            </p>
							                        </td>
							                    </tr>

							                </table>
							            </td>
							        </tr>
							    </table>
							</body>
							</html>
							""",
					otpCode);

			helper.setText(htmlContent, true);

			mailSender.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException("Không thể gửi email", e);
		}
	}
}
