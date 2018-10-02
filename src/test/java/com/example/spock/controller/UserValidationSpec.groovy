package com.example.spock.controller

import com.example.spock.BaseWebMvcSpec
import com.example.spock.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import spock.lang.Unroll
import spock.mock.DetachedMockFactory

import static groovy.json.JsonOutput.toJson
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(controllers = [UserController])
class UserValidationSpec extends BaseWebMvcSpec {

    @Autowired
    ObjectMapper objectMapper

    @Unroll
    def "should not allow to create a user with an invalid email address: #emailAddress - spock assertions"() {
        given:
        Map request = [
                email_address : emailAddress,
                name          : 'John',
                last_name     : 'Wayne'
        ]

        when:
        def result = doRequest(
                post('/users').contentType(APPLICATION_JSON).content(toJson(request))
        ).andReturn()

        then:
        result.response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()

        and:
        with (objectMapper.readValue(result.response.contentAsString, Map)) {
            it.errors[0].path == 'emailAddress'
            it.errors[0].userMessage == userMessage
        }

        where:
        emailAddress              || userMessage
        'john.wayne(at)gmail.com' || 'Invalid email address.'
        'abcdefg'                 || 'Invalid email address.'
        ''                        || 'Invalid email address.'
        null                      || 'Email must be provided.'
    }

    @Unroll
    def "should not allow to create a user with an invalid name: #name"() {
        given:
        Map request = [
                email_address : 'john.wayne@gmail.com',
                name          : name,
                last_name     : 'Wayne'
        ]

        when:
        def results = doRequest(
                post('/users').contentType(APPLICATION_JSON).content(toJson(request))
        )

        then:
        results.andExpect(status().isUnprocessableEntity())

        and:
        results.andExpect(jsonPath('$.errors[0].code').value('MethodArgumentNotValidException'))
        results.andExpect(jsonPath('$.errors[0].path').value('name'))
        results.andExpect(jsonPath('$.errors[0].userMessage').value(userMessage))

        where:
        name      || userMessage
        null      || 'Name must be provided.'
        'I'       || 'Name must be at least 2 characters and at most 50 characters long.'
        ''        || 'Name must be at least 2 characters and at most 50 characters long.'
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        UserService registrationService() {
            return detachedMockFactory.Stub(UserService)
        }
    }
}
