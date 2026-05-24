#!/usr/bin/env python3
"""Generate a minimal but correct project.pbxproj for the GymBuddy iOS app."""

import os
import hashlib

ROOT = "/home/claude/repo/ios/GymBuddy"
TARGET_NAME = "GymBuddy"
BUNDLE_ID = "app.gymbuddy.GymBuddy"
DEPLOYMENT_TARGET = "17.0"

NL = "\n"
TAB = "\t"

# Walk the GymBuddy folder for source files
swift_files = []
plist_file = None
asset_dir = None
strings_files = []  # tuples (filepath, lproj)

for dirpath, dirnames, filenames in os.walk(os.path.join(ROOT, TARGET_NAME)):
    if "Assets.xcassets" in dirpath:
        if dirpath.endswith("Assets.xcassets"):
            asset_dir = dirpath
        continue
    for fn in filenames:
        full = os.path.join(dirpath, fn)
        rel = os.path.relpath(full, os.path.join(ROOT, TARGET_NAME))
        if fn.endswith(".swift"):
            swift_files.append(rel)
        elif fn == "Info.plist":
            plist_file = rel
        elif fn.endswith(".strings"):
            parent = os.path.basename(dirpath)
            strings_files.append((rel, parent))

swift_files.sort()
strings_files.sort()
print(f"Found {len(swift_files)} swift files, plist={plist_file}, strings={len(strings_files)}")

def uid(name, salt=""):
    h = hashlib.md5((salt + name).encode()).hexdigest().upper()
    return h[:24]

PROJECT_ID = uid("project")
MAIN_GROUP_ID = uid("main")
PRODUCTS_GROUP_ID = uid("products")
APP_PRODUCT_ID = uid("app-product")
TARGET_ID = uid("target")
SOURCES_PHASE_ID = uid("sources-phase")
RESOURCES_PHASE_ID = uid("resources-phase")
FRAMEWORKS_PHASE_ID = uid("frameworks-phase")
DEBUG_PROJ_CONF_ID = uid("debug-proj-conf")
RELEASE_PROJ_CONF_ID = uid("release-proj-conf")
PROJ_CONF_LIST_ID = uid("proj-conf-list")
DEBUG_TGT_CONF_ID = uid("debug-tgt-conf")
RELEASE_TGT_CONF_ID = uid("release-tgt-conf")
TGT_CONF_LIST_ID = uid("tgt-conf-list")
ASSETS_FILE_ID = uid("assets-file")
ASSETS_BUILD_ID = uid("assets-build")

class Group:
    def __init__(self, name, path=None):
        self.name = name
        self.path = path
        self.children = {}
        self.files = []
        self.id = uid("group:" + name + (path or ""))

root_group = Group("GymBuddy", path="GymBuddy")

file_refs = []
build_files = []

for rel in swift_files:
    parts = rel.split("/")
    folder_parts = parts[:-1]
    fname = parts[-1]
    file_ref_id = uid("file:" + rel)
    build_id = uid("build:" + rel)
    file_refs.append((file_ref_id, fname, rel, "sourcecode.swift"))
    build_files.append((build_id, file_ref_id, fname))

    cur = root_group
    for p in folder_parts:
        if p not in cur.children:
            cur.children[p] = Group(p, path=p)
        cur = cur.children[p]
    cur.files.append((fname, file_ref_id))

variant_group_id = uid("variant:Localizable.strings")
variant_children = []  # (file_ref_id, lang, rel)
for rel, lproj in strings_files:
    lang = lproj.replace(".lproj", "")
    ref_id = uid("strings:" + rel)
    variant_children.append((ref_id, lang, rel))

loc_build_id = uid("build:Localizable.strings")
info_plist_ref_id = uid("file:Info.plist") if plist_file else None

# Helper to emit a group block (multi-line strings; avoid \n in f-string expressions)
def group_block(group, top_extras=None):
    lines = ["\t\t" + group.id + " /* " + group.name + " */ = {",
             "\t\t\tisa = PBXGroup;",
             "\t\t\tchildren = ("]
    for name in sorted(group.children.keys()):
        sub = group.children[name]
        lines.append("\t\t\t\t" + sub.id + " /* " + name + " */,")
    for fname, ref_id in sorted(group.files):
        lines.append("\t\t\t\t" + ref_id + " /* " + fname + " */,")
    if top_extras:
        for extra_id, name in top_extras:
            lines.append("\t\t\t\t" + extra_id + " /* " + name + " */,")
    lines.append("\t\t\t);")
    if group.path:
        lines.append("\t\t\tpath = \"" + group.path + "\";")
    if group.path and group.name and group.name != group.path:
        lines.append("\t\t\tname = \"" + group.name + "\";")
    lines.append("\t\t\tsourceTree = \"<group>\";")
    lines.append("\t\t};")
    return NL.join(lines)

def emit_group_blocks(group, is_top, extras=None):
    blocks = [group_block(group, extras if is_top else None)]
    for sub in group.children.values():
        blocks.extend(emit_group_blocks(sub, False))
    return blocks

# File refs section
file_ref_lines = []
for ref_id, fname, path, ftype in file_refs:
    # sourceTree=<group> makes `path` relative to the parent group's own path.
    # Since each Swift file already lives in a group whose path is its directory,
    # the file's path must be just the basename — otherwise Xcode doubles the dir.
    file_ref_lines.append(
        "\t\t" + ref_id + " /* " + fname + " */ = {isa = PBXFileReference; lastKnownFileType = " + ftype +
        "; path = \"" + fname + "\"; sourceTree = \"<group>\"; };"
    )
if plist_file:
    file_ref_lines.append(
        "\t\t" + info_plist_ref_id + " /* Info.plist */ = {isa = PBXFileReference; lastKnownFileType = text.plist.xml; path = \"Info.plist\"; sourceTree = \"<group>\"; };"
    )
file_ref_lines.append(
    "\t\t" + ASSETS_FILE_ID + " /* Assets.xcassets */ = {isa = PBXFileReference; lastKnownFileType = folder.assetcatalog; path = \"Assets.xcassets\"; sourceTree = \"<group>\"; };"
)
file_ref_lines.append(
    "\t\t" + APP_PRODUCT_ID + " /* GymBuddy.app */ = {isa = PBXFileReference; explicitFileType = wrapper.application; includeInIndex = 0; path = \"GymBuddy.app\"; sourceTree = BUILT_PRODUCTS_DIR; };"
)
for ref_id, lang, rel in variant_children:
    file_ref_lines.append(
        "\t\t" + ref_id + " /* " + lang + " */ = {isa = PBXFileReference; lastKnownFileType = text.plist.strings; name = \"" + lang + "\"; path = \"" + rel + "\"; sourceTree = \"<group>\"; };"
    )

# Build files
build_file_lines = []
for bid, ref_id, name in build_files:
    build_file_lines.append("\t\t" + bid + " /* " + name + " in Sources */ = {isa = PBXBuildFile; fileRef = " + ref_id + "; };")
build_file_lines.append("\t\t" + ASSETS_BUILD_ID + " /* Assets.xcassets in Resources */ = {isa = PBXBuildFile; fileRef = " + ASSETS_FILE_ID + "; };")
if variant_children:
    build_file_lines.append("\t\t" + loc_build_id + " /* Localizable.strings in Resources */ = {isa = PBXBuildFile; fileRef = " + variant_group_id + "; };")

# Variant group block
variant_section_lines = []
if variant_children:
    variant_section_lines.append("\t\t" + variant_group_id + " /* Localizable.strings */ = {")
    variant_section_lines.append("\t\t\tisa = PBXVariantGroup;")
    variant_section_lines.append("\t\t\tchildren = (")
    for ref_id, lang, _ in variant_children:
        variant_section_lines.append("\t\t\t\t" + ref_id + " /* " + lang + " */,")
    variant_section_lines.append("\t\t\t);")
    variant_section_lines.append("\t\t\tname = \"Localizable.strings\";")
    variant_section_lines.append("\t\t\tsourceTree = \"<group>\";")
    variant_section_lines.append("\t\t};")

# Sources / Resources phase files
src_lines = ["\t\t\t\t" + bid + " /* " + n + " in Sources */," for bid, _, n in build_files]
res_lines = ["\t\t\t\t" + ASSETS_BUILD_ID + " /* Assets.xcassets in Resources */,"]
if variant_children:
    res_lines.append("\t\t\t\t" + loc_build_id + " /* Localizable.strings in Resources */,")

# Top-level extras under the GymBuddy group
extras = []
if plist_file:
    extras.append((info_plist_ref_id, "Info.plist"))
if variant_children:
    extras.append((variant_group_id, "Localizable.strings"))
extras.append((ASSETS_FILE_ID, "Assets.xcassets"))

group_blocks = NL.join(emit_group_blocks(root_group, True, extras))

# Compose final
out = []
out.append("// !$*UTF8*$!")
out.append("{")
out.append("\tarchiveVersion = 1;")
out.append("\tclasses = {")
out.append("\t};")
out.append("\tobjectVersion = 56;")
out.append("\tobjects = {")
out.append("")
out.append("/* Begin PBXBuildFile section */")
out.extend(build_file_lines)
out.append("/* End PBXBuildFile section */")
out.append("")
out.append("/* Begin PBXFileReference section */")
out.extend(file_ref_lines)
out.append("/* End PBXFileReference section */")
out.append("")
out.append("/* Begin PBXFrameworksBuildPhase section */")
out.append("\t\t" + FRAMEWORKS_PHASE_ID + " /* Frameworks */ = {")
out.append("\t\t\tisa = PBXFrameworksBuildPhase;")
out.append("\t\t\tbuildActionMask = 2147483647;")
out.append("\t\t\tfiles = (")
out.append("\t\t\t);")
out.append("\t\t\trunOnlyForDeploymentPostprocessing = 0;")
out.append("\t\t};")
out.append("/* End PBXFrameworksBuildPhase section */")
out.append("")
out.append("/* Begin PBXGroup section */")
out.append("\t\t" + MAIN_GROUP_ID + " = {")
out.append("\t\t\tisa = PBXGroup;")
out.append("\t\t\tchildren = (")
out.append("\t\t\t\t" + root_group.id + " /* GymBuddy */,")
out.append("\t\t\t\t" + PRODUCTS_GROUP_ID + " /* Products */,")
out.append("\t\t\t);")
out.append("\t\t\tsourceTree = \"<group>\";")
out.append("\t\t};")
out.append("\t\t" + PRODUCTS_GROUP_ID + " /* Products */ = {")
out.append("\t\t\tisa = PBXGroup;")
out.append("\t\t\tchildren = (")
out.append("\t\t\t\t" + APP_PRODUCT_ID + " /* GymBuddy.app */,")
out.append("\t\t\t);")
out.append("\t\t\tname = Products;")
out.append("\t\t\tsourceTree = \"<group>\";")
out.append("\t\t};")
out.append(group_blocks)
out.append("/* End PBXGroup section */")
out.append("")
if variant_section_lines:
    out.append("/* Begin PBXVariantGroup section */")
    out.extend(variant_section_lines)
    out.append("/* End PBXVariantGroup section */")
    out.append("")
out.append("/* Begin PBXNativeTarget section */")
out.append("\t\t" + TARGET_ID + " /* GymBuddy */ = {")
out.append("\t\t\tisa = PBXNativeTarget;")
out.append("\t\t\tbuildConfigurationList = " + TGT_CONF_LIST_ID + " /* Build configuration list for PBXNativeTarget \"GymBuddy\" */;")
out.append("\t\t\tbuildPhases = (")
out.append("\t\t\t\t" + SOURCES_PHASE_ID + " /* Sources */,")
out.append("\t\t\t\t" + FRAMEWORKS_PHASE_ID + " /* Frameworks */,")
out.append("\t\t\t\t" + RESOURCES_PHASE_ID + " /* Resources */,")
out.append("\t\t\t);")
out.append("\t\t\tbuildRules = (")
out.append("\t\t\t);")
out.append("\t\t\tdependencies = (")
out.append("\t\t\t);")
out.append("\t\t\tname = GymBuddy;")
out.append("\t\t\tproductName = GymBuddy;")
out.append("\t\t\tproductReference = " + APP_PRODUCT_ID + " /* GymBuddy.app */;")
out.append("\t\t\tproductType = \"com.apple.product-type.application\";")
out.append("\t\t};")
out.append("/* End PBXNativeTarget section */")
out.append("")
out.append("/* Begin PBXProject section */")
out.append("\t\t" + PROJECT_ID + " /* Project object */ = {")
out.append("\t\t\tisa = PBXProject;")
out.append("\t\t\tattributes = {")
out.append("\t\t\t\tBuildIndependentTargetsInParallel = 1;")
out.append("\t\t\t\tLastSwiftUpdateCheck = 1500;")
out.append("\t\t\t\tLastUpgradeCheck = 1500;")
out.append("\t\t\t\tTargetAttributes = {")
out.append("\t\t\t\t\t" + TARGET_ID + " = {")
out.append("\t\t\t\t\t\tCreatedOnToolsVersion = 15.0;")
out.append("\t\t\t\t\t};")
out.append("\t\t\t\t};")
out.append("\t\t\t};")
out.append("\t\t\tbuildConfigurationList = " + PROJ_CONF_LIST_ID + " /* Build configuration list for PBXProject \"GymBuddy\" */;")
out.append("\t\t\tcompatibilityVersion = \"Xcode 14.0\";")
out.append("\t\t\tdevelopmentRegion = en;")
out.append("\t\t\thasScannedForEncodings = 0;")
out.append("\t\t\tknownRegions = (")
out.append("\t\t\t\ten,")
out.append("\t\t\t\tru,")
out.append("\t\t\t\tBase,")
out.append("\t\t\t);")
out.append("\t\t\tmainGroup = " + MAIN_GROUP_ID + ";")
out.append("\t\t\tproductRefGroup = " + PRODUCTS_GROUP_ID + " /* Products */;")
out.append("\t\t\tprojectDirPath = \"\";")
out.append("\t\t\tprojectRoot = \"\";")
out.append("\t\t\ttargets = (")
out.append("\t\t\t\t" + TARGET_ID + " /* GymBuddy */,")
out.append("\t\t\t);")
out.append("\t\t};")
out.append("/* End PBXProject section */")
out.append("")
out.append("/* Begin PBXResourcesBuildPhase section */")
out.append("\t\t" + RESOURCES_PHASE_ID + " /* Resources */ = {")
out.append("\t\t\tisa = PBXResourcesBuildPhase;")
out.append("\t\t\tbuildActionMask = 2147483647;")
out.append("\t\t\tfiles = (")
out.extend(res_lines)
out.append("\t\t\t);")
out.append("\t\t\trunOnlyForDeploymentPostprocessing = 0;")
out.append("\t\t};")
out.append("/* End PBXResourcesBuildPhase section */")
out.append("")
out.append("/* Begin PBXSourcesBuildPhase section */")
out.append("\t\t" + SOURCES_PHASE_ID + " /* Sources */ = {")
out.append("\t\t\tisa = PBXSourcesBuildPhase;")
out.append("\t\t\tbuildActionMask = 2147483647;")
out.append("\t\t\tfiles = (")
out.extend(src_lines)
out.append("\t\t\t);")
out.append("\t\t\trunOnlyForDeploymentPostprocessing = 0;")
out.append("\t\t};")
out.append("/* End PBXSourcesBuildPhase section */")
out.append("")
out.append("/* Begin XCBuildConfiguration section */")
# Project Debug
out.extend([
    "\t\t" + DEBUG_PROJ_CONF_ID + " /* Debug */ = {",
    "\t\t\tisa = XCBuildConfiguration;",
    "\t\t\tbuildSettings = {",
    "\t\t\t\tALWAYS_SEARCH_USER_PATHS = NO;",
    "\t\t\t\tCLANG_ANALYZER_NONNULL = YES;",
    "\t\t\t\tCLANG_ENABLE_MODULES = YES;",
    "\t\t\t\tCLANG_ENABLE_OBJC_ARC = YES;",
    "\t\t\t\tCOPY_PHASE_STRIP = NO;",
    "\t\t\t\tDEBUG_INFORMATION_FORMAT = dwarf;",
    "\t\t\t\tENABLE_STRICT_OBJC_MSGSEND = YES;",
    "\t\t\t\tENABLE_TESTABILITY = YES;",
    "\t\t\t\tGCC_C_LANGUAGE_STANDARD = gnu17;",
    "\t\t\t\tGCC_DYNAMIC_NO_PIC = NO;",
    "\t\t\t\tGCC_NO_COMMON_BLOCKS = YES;",
    "\t\t\t\tGCC_OPTIMIZATION_LEVEL = 0;",
    "\t\t\t\tGCC_PREPROCESSOR_DEFINITIONS = (",
    "\t\t\t\t\t\"DEBUG=1\",",
    "\t\t\t\t\t\"$(inherited)\",",
    "\t\t\t\t);",
    "\t\t\t\tIPHONEOS_DEPLOYMENT_TARGET = " + DEPLOYMENT_TARGET + ";",
    "\t\t\t\tMTL_ENABLE_DEBUG_INFO = INCLUDE_SOURCE;",
    "\t\t\t\tONLY_ACTIVE_ARCH = YES;",
    "\t\t\t\tSDKROOT = iphoneos;",
    "\t\t\t\tSWIFT_ACTIVE_COMPILATION_CONDITIONS = \"DEBUG $(inherited)\";",
    "\t\t\t\tSWIFT_OPTIMIZATION_LEVEL = \"-Onone\";",
    "\t\t\t};",
    "\t\t\tname = Debug;",
    "\t\t};",
])
# Project Release
out.extend([
    "\t\t" + RELEASE_PROJ_CONF_ID + " /* Release */ = {",
    "\t\t\tisa = XCBuildConfiguration;",
    "\t\t\tbuildSettings = {",
    "\t\t\t\tALWAYS_SEARCH_USER_PATHS = NO;",
    "\t\t\t\tCLANG_ANALYZER_NONNULL = YES;",
    "\t\t\t\tCOPY_PHASE_STRIP = NO;",
    "\t\t\t\tDEBUG_INFORMATION_FORMAT = \"dwarf-with-dsym\";",
    "\t\t\t\tENABLE_NS_ASSERTIONS = NO;",
    "\t\t\t\tENABLE_STRICT_OBJC_MSGSEND = YES;",
    "\t\t\t\tGCC_C_LANGUAGE_STANDARD = gnu17;",
    "\t\t\t\tGCC_NO_COMMON_BLOCKS = YES;",
    "\t\t\t\tIPHONEOS_DEPLOYMENT_TARGET = " + DEPLOYMENT_TARGET + ";",
    "\t\t\t\tMTL_ENABLE_DEBUG_INFO = NO;",
    "\t\t\t\tSDKROOT = iphoneos;",
    "\t\t\t\tSWIFT_COMPILATION_MODE = wholemodule;",
    "\t\t\t\tSWIFT_OPTIMIZATION_LEVEL = \"-O\";",
    "\t\t\t\tVALIDATE_PRODUCT = YES;",
    "\t\t\t};",
    "\t\t\tname = Release;",
    "\t\t};",
])
# Target Debug
out.extend([
    "\t\t" + DEBUG_TGT_CONF_ID + " /* Debug */ = {",
    "\t\t\tisa = XCBuildConfiguration;",
    "\t\t\tbuildSettings = {",
    "\t\t\t\tASSETCATALOG_COMPILER_APPICON_NAME = AppIcon;",
    "\t\t\t\tASSETCATALOG_COMPILER_GLOBAL_ACCENT_COLOR_NAME = AccentColor;",
    "\t\t\t\tCODE_SIGN_STYLE = Automatic;",
    "\t\t\t\tCURRENT_PROJECT_VERSION = 1;",
    "\t\t\t\tDEVELOPMENT_ASSET_PATHS = \"\";",
    "\t\t\t\tENABLE_PREVIEWS = YES;",
    "\t\t\t\tGENERATE_INFOPLIST_FILE = NO;",
    "\t\t\t\tINFOPLIST_FILE = \"GymBuddy/Info.plist\";",
    "\t\t\t\tLD_RUNPATH_SEARCH_PATHS = (",
    "\t\t\t\t\t\"$(inherited)\",",
    "\t\t\t\t\t\"@executable_path/Frameworks\",",
    "\t\t\t\t);",
    "\t\t\t\tMARKETING_VERSION = 1.0;",
    "\t\t\t\tPRODUCT_BUNDLE_IDENTIFIER = \"" + BUNDLE_ID + "\";",
    "\t\t\t\tPRODUCT_NAME = \"$(TARGET_NAME)\";",
    "\t\t\t\tSWIFT_EMIT_LOC_STRINGS = YES;",
    "\t\t\t\tSWIFT_VERSION = 5.9;",
    "\t\t\t\tTARGETED_DEVICE_FAMILY = 1;",
    "\t\t\t};",
    "\t\t\tname = Debug;",
    "\t\t};",
])
# Target Release
out.extend([
    "\t\t" + RELEASE_TGT_CONF_ID + " /* Release */ = {",
    "\t\t\tisa = XCBuildConfiguration;",
    "\t\t\tbuildSettings = {",
    "\t\t\t\tASSETCATALOG_COMPILER_APPICON_NAME = AppIcon;",
    "\t\t\t\tASSETCATALOG_COMPILER_GLOBAL_ACCENT_COLOR_NAME = AccentColor;",
    "\t\t\t\tCODE_SIGN_STYLE = Automatic;",
    "\t\t\t\tCURRENT_PROJECT_VERSION = 1;",
    "\t\t\t\tDEVELOPMENT_ASSET_PATHS = \"\";",
    "\t\t\t\tENABLE_PREVIEWS = YES;",
    "\t\t\t\tGENERATE_INFOPLIST_FILE = NO;",
    "\t\t\t\tINFOPLIST_FILE = \"GymBuddy/Info.plist\";",
    "\t\t\t\tLD_RUNPATH_SEARCH_PATHS = (",
    "\t\t\t\t\t\"$(inherited)\",",
    "\t\t\t\t\t\"@executable_path/Frameworks\",",
    "\t\t\t\t);",
    "\t\t\t\tMARKETING_VERSION = 1.0;",
    "\t\t\t\tPRODUCT_BUNDLE_IDENTIFIER = \"" + BUNDLE_ID + "\";",
    "\t\t\t\tPRODUCT_NAME = \"$(TARGET_NAME)\";",
    "\t\t\t\tSWIFT_EMIT_LOC_STRINGS = YES;",
    "\t\t\t\tSWIFT_VERSION = 5.9;",
    "\t\t\t\tTARGETED_DEVICE_FAMILY = 1;",
    "\t\t\t};",
    "\t\t\tname = Release;",
    "\t\t};",
])
out.append("/* End XCBuildConfiguration section */")
out.append("")
out.append("/* Begin XCConfigurationList section */")
out.extend([
    "\t\t" + PROJ_CONF_LIST_ID + " /* Build configuration list for PBXProject \"GymBuddy\" */ = {",
    "\t\t\tisa = XCConfigurationList;",
    "\t\t\tbuildConfigurations = (",
    "\t\t\t\t" + DEBUG_PROJ_CONF_ID + " /* Debug */,",
    "\t\t\t\t" + RELEASE_PROJ_CONF_ID + " /* Release */,",
    "\t\t\t);",
    "\t\t\tdefaultConfigurationIsVisible = 0;",
    "\t\t\tdefaultConfigurationName = Release;",
    "\t\t};",
    "\t\t" + TGT_CONF_LIST_ID + " /* Build configuration list for PBXNativeTarget \"GymBuddy\" */ = {",
    "\t\t\tisa = XCConfigurationList;",
    "\t\t\tbuildConfigurations = (",
    "\t\t\t\t" + DEBUG_TGT_CONF_ID + " /* Debug */,",
    "\t\t\t\t" + RELEASE_TGT_CONF_ID + " /* Release */,",
    "\t\t\t);",
    "\t\t\tdefaultConfigurationIsVisible = 0;",
    "\t\t\tdefaultConfigurationName = Release;",
    "\t\t};",
])
out.append("/* End XCConfigurationList section */")
out.append("\t};")
out.append("\trootObject = " + PROJECT_ID + " /* Project object */;")
out.append("}")

out_path = os.path.join(ROOT, "GymBuddy.xcodeproj", "project.pbxproj")
os.makedirs(os.path.dirname(out_path), exist_ok=True)
with open(out_path, "w") as f:
    f.write(NL.join(out) + NL)
print(f"Wrote {out_path}")
print(f"  Swift files referenced: {len(swift_files)}")
print(f"  Localizations: {len(variant_children)}")
