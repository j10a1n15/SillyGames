package gay.j10a1n15.sillygames.rpc

data class RpcInfo(
    var firstLine: String,
    var secondLine: String,

    var start: Long,
    var end: Long? = null,

    var thumbnail: String? = null,
    var icon: String? = null,
)
