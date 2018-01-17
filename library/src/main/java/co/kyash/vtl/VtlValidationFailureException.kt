package co.kyash.vtl

class VtlValidationFailureException(
        private val errorMessage: String
) : RuntimeException(errorMessage) {

    companion object {
        fun getErrorMessage(throwable: Throwable?): String? {
            var errorMessage: String? = null
            var cause: Throwable? = throwable

            while (cause != null) {
                if (cause is VtlValidationFailureException) {
                    errorMessage = cause.errorMessage
                    break
                }

                cause = cause.cause
            }

            return errorMessage
        }
    }
}