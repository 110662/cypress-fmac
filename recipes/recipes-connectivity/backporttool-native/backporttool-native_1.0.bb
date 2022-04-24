SUMMARY = "Cypress Backports tool"
LICENSE = "GPLv2.0"
LIC_FILES_CHKSUM = "file://${WORKDIR}/v5.4.18-backports/COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

inherit systemd
inherit native

SYSTEMD_AUTO_ENABLE = "disable"

SRC_URI = "file://%F_BACKPORTS_FILE% \
		file://0001-wa-makefile-include.patch;patchdir=${WORKDIR}/%F_PATCH_DIR% \
		file://0002-req-fw-direct-war.patch;patchdir=${WORKDIR}/%F_PATCH_DIR% \
		"

S = "${WORKDIR}/backporttool-native-${PV}"
B = "${WORKDIR}/backporttool-native-${PV}/"

DEPENDS += "flex-native bison-native"
DEPENDS += "linux-imx"

inherit module

EXTRA_OEMAKE += "LEX=flex"

do_configure () {
	echo "Configuring"
	echo "KLIB:       ${KLIB}"
	echo "KLIB_BUILD: ${KLIB_BUILD} "
}

export EXTRA_CFLAGS = "${CFLAGS}"
export BINDIR = "${sbindir}"

do_compile () {
	echo "Compiling: "
	echo "KLIB:       ${KLIB}"
	echo "KLIB_BUILD: ${KLIB_BUILD} "
	echo "KBUILD_OUTPUT: ${KBUILD_OUTPUT}"

	rm -rf .git

	cp -a ${WORKDIR}/%F_PATCH_DIR%/. .

	oe_runmake KLIB="${STAGING_KERNEL_DIR}" KLIB_BUILD="${STAGING_KERNEL_BUILDDIR}" defconfig-brcmfmac
	oe_runmake KLIB="${STAGING_KERNEL_DIR}" KLIB_BUILD="${STAGING_KERNEL_BUILDDIR}" oldconfig
}

do_install () {
	echo "Installing: "
	install -d ${D}${sbindir}

}

FILES_${PN} += "${sbindir}"
PACKAGES += "FILES-${PN}"

