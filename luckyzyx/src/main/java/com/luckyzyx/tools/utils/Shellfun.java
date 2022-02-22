package com.luckyzyx.tools.utils;

public class Shellfun {
    //读取module.prop
    public static String grep_prop(){
        return "grep_prop() {\n" +
                "  if [[ -z \"$2\" ]]; then\n" +
                "    which getprop &>/dev/null && getprop \"$1\" || magisk resetprop \"$1\"\n" +
                "    return $?\n" +
                "  elif [[ -f \"$2\" ]]; then\n" +
                "    result=$(sed -rn \"s/^$1=//p\" \"$2\" | head -n 1)\n" +
                "    [[ -z \"$result\" ]] && return 1\n" +
                "    cat <<-zyx\n" +
                "\t\t\t$result\n" +
                "\t\tzyx\n" +
                "  else\n" +
                "    return $2\n" +
                "  fi\n" +
                "  return 0\n" +
                "}";
    }

    //判断目录/文件类型
    public static String ifexist(){
        return "ifexist() {\n" +
                "if [[ -d $1 ]]; then\n" +
                "  return 1\n" +
                "elif [[ -f $1 ]]; then\n" +
                "  return 2\n" +
                "else\n" +
                "  return 0\n" +
                "fi\n" +
                "}";
    }

}
