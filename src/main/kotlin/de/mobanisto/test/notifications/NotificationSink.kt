package de.mobanisto.test.notifications

interface NotificationSink {

    /**
     * true, if the [NotificationSink] has been successfully initialized
     * and is ready to show notifications. false otherwise
     */
    val available: Boolean

    /**
     * if [available] is false, contains a message explaining the problem
     * as shown to the user
     */
    val errorMessage: String

    fun init()
    fun uninit()

    fun notify(message: String)
}

object StubNotificationSink : NotificationSink {
    override val available: Boolean
        get() = false

    override val errorMessage: String
        get() = "Stub notification sink"

    override fun init() {}
    override fun uninit() {}
    override fun notify(message: String) {}
}
