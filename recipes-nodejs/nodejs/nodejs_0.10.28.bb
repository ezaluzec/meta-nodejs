DESCRIPTION = "nodeJS Evented I/O for V8 JavaScript"
HOMEPAGE = "http://nodejs.org"
LICENSE = "MIT & BSD & Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4a31e6c424761191227143b86f58a1ef"

PR = "r1"

DEPENDS = "zlib openssl"

SRC_URI = "http://nodejs.org/dist/v${PV}/node-v${PV}.tar.gz"

SRC_URI[md5sum] = "87768be7065d2120e71619948ab4bb2d"
SRC_URI[sha256sum] = "abddc6441e0f208f6ed8a045e0293f713ea7f6dfb2d6a9a2024bf8b1b4617710"

S = "${WORKDIR}/node-v${PV}"

# v8 errors out if you have set CCACHE
CCACHE = ""

ARCHFLAGS_arm = "${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', '--with-arm-float-abi=hard', '--with-arm-float-abi=softfp', d)}"
ARCHFLAGS ?= ""

# Node is way too cool to use proper autotools, so we install two wrappers to forcefully inject proper arch cflags to workaround gypi
do_configure () {
  export LD="${CXX}"

  ./configure --prefix=${prefix} --without-snapshot --shared-zlib --shared-openssl ${ARCHFLAGS}
}

do_compile () {
  export LD="${CXX}"
  make BUILDTYPE=Release
}

do_install () {
  DESTDIR=${D} oe_runmake install
}

RDEPENDS_${PN} = "zlib openssl curl python-compiler python-shell python-datetime python-subprocess python-multiprocessing python-crypt python-textutils python-netclient python-misc"
RDEPENDS_${PN}_virtclass-native = "curl-native python-native"

FILES_${PN} += "${libdir}/node/wafadmin ${libdir}/node_modules ${libdir}/dtrace"
BBCLASSEXTEND = "native"
