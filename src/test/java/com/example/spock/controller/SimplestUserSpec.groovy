package com.example.spock.controller

import com.example.spock.domain.User
import com.example.spock.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static groovy.json.JsonOutput.toJson
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [UserController])
class SimplestUserSpec extends Specification {

    @Autowired
    protected MockMvc mvc

    @Autowired
    UserService userService

    @Autowired
    ObjectMapper objectMapper

    def "should pass user user details to domain component and return 'created' status"() {
        given:
        Map request = [
                email_address: 'john.wayne@gmail.com',
                name         : 'John',
                last_name    : 'Wayne'
        ]

        and:
        userService.saveUser('john.wayne@gmail.com', 'John', 'Wayne') >> new User(
                'user-id-2',
                'john.wayne@gmail.com',
                'John',
                'Wayne'
        )

        when:
        def results = mvc.perform(post('/users').contentType(APPLICATION_JSON).content(toJson(request)))

        then:
        results.andExpect(status().isCreated())

        and:
        results.andExpect(jsonPath('$.user_id').value('user-id-2'))
        results.andExpect(jsonPath('$.email_address').value('john.wayne@gmail.com'))
        results.andExpect(jsonPath('$.name').value('John'))
        results.andExpect(jsonPath('$.last_name').value('Wayne'))
    }

    def "should pass user details to domain component and return 'created' status - spock assertions"() {
        given:
        Map request = [
                email_address: 'john.wayne@gmail.com',
                name         : 'John',
                last_name    : 'Wayne'
        ]

        and:
        userService.saveUser('john.wayne@gmail.com', 'John', 'Wayne') >> new User(
                'user-id-1',
                'john.wayne@gmail.com',
                'John',
                'Wayne'
        )

        when:
        def response = mvc.perform(
                post('/users').contentType(APPLICATION_JSON).content(toJson(request))
        ).andReturn().response  // notice the extra call to: andReturn()

        then:
        response.status == HttpStatus.CREATED.value()

        and:
        with(objectMapper.readValue(response.contentAsString, Map)) {
            it.user_id == 'user-id-1'
            it.email_address == 'john.wayne@gmail.com'
            it.name == 'John'
            it.last_name == 'Wayne'
        }
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
