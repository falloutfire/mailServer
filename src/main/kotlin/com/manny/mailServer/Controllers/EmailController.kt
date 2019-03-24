package com.manny.mailServer.Controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.manny.mailServer.Entities.Mail
import org.springframework.http.HttpStatus
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream


@RestController
@RequestMapping("mail/")
class EmailController(private val emailSender: JavaMailSender) {

    @RequestMapping(value = ["/sendEmailWithAttach"], method = [RequestMethod.POST], consumes = ["multipart/form-data"])
    fun sendEmailWithAttachTest(@RequestPart("mail") mailString: String, @RequestPart("file") file: MultipartFile?): ApiResponse {

        val mapper = ObjectMapper()
        val mail = mapper.readValue(mailString, Mail::class.java)

        val sender = emailSender as JavaMailSenderImpl
        sender.username = mail.fromUser.email
        sender.password = mail.fromUser.password


        /*if (file != null) {
            convFile = File(file.originalFilename)
            convFile.createNewFile()
            val fos = FileOutputStream(convFile)
            fos.write(file.bytes)
            fos.close()
        }*/

        val arrayErrorsEmails = ArrayList<String>()

        mail.toUser.forEach {
            val message = emailSender.createMimeMessage()
            val multipart = true

            val helper = MimeMessageHelper(message, multipart)
            helper.setTo(it.email)
            helper.setSubject(mail.theme)
            helper.setText(mail.body)
            file?.let {
                val convFile = File(file.originalFilename)
                convFile.createNewFile()
                val fos = FileOutputStream(convFile)
                fos.write(file.bytes)
                fos.close()
                helper.addAttachment(convFile.name, convFile) }
            try {
                sender.send(message)
            } catch (e: MailException){
                arrayErrorsEmails.add(it.email)
            }

        }
        return if (arrayErrorsEmails.isEmpty())
            ApiResponse(HttpStatus.OK, "SUCCESS")
        else
            ApiResponse(HttpStatus.OK, "Сообщения к этим Email не были отправлены", arrayErrorsEmails)
    }
}



