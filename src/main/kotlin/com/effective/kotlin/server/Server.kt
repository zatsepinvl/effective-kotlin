package com.effective.kotlin.server

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

fun main() {
    val server = ServerSocket(8080)
    while (true) {
        val socket = server.accept()
        handleRequest(socket)
    }
}

private fun handleRequest(socket: Socket) {
    // Read the request from the client
    val request = StringBuilder()
    val input: InputStream = socket.getInputStream()
    val reader = BufferedReader(InputStreamReader(input))
    var line: String
    while (reader.readLine().also { line = it } != null && !line.isEmpty()) {
        request.append(line).append("\r\n")
    }
    println("Received request:\n$request")

    // Prepare the response
    val response = """
         HTTP/1.1 200 OK
         Content-Type: text/plain
         Content-Length: 12
         
         Hello World!
         """.trimIndent()

    // Send the response to the client
    val output = socket.getOutputStream()
    output.write(response.toByteArray(charset("UTF-8")))

    // Close the client socket
    socket.close()
}