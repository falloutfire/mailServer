package com.manny.mailServer.Controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.manny.mailServer.Entities.Mail
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
    fun sendEmailWithAttachTest(@RequestPart("mail") mailString: String, @RequestPart("file") file: MultipartFile?): String {

        val mapper = ObjectMapper()
        val mail = mapper.readValue(mailString, Mail::class.java)

        val sender = emailSender as JavaMailSenderImpl
        sender.username = mail.fromUser.email
        sender.password = mail.fromUser.password
        var convFile: File? = null

        if (file != null) {
            convFile = File(file.originalFilename)
            convFile.createNewFile()
            val fos = FileOutputStream(convFile)
            fos.write(file.bytes)
            fos.close()
        }


        mail.toUser.forEach {
            val message = emailSender.createMimeMessage()
            val multipart = true

            val helper = MimeMessageHelper(message, multipart)
            helper.setTo(it.email)
            helper.setSubject(mail.theme)
            helper.setText(mail.body)
            convFile?.let { helper.addAttachment(convFile.name, convFile) }
            sender.send(message)
        }

        return "email send"
    }
}



