package com.tyut.judge.codesendbox;


import com.tyut.judge.codesendbox.impl.ExampleCodeSendBox;
import com.tyut.judge.codesendbox.impl.RemoteCodeSendBox;
import com.tyut.judge.codesendbox.impl.ThirdPartyCodeSendBox;

/**
 * @version v1.0
 * @author OldGj 2024/12/23
 * @apiNote 代码沙箱工厂
 */
public class CodeSendBoxFactory {

    /**
     * 工厂类创建不同的代码沙箱
     * @param type
     * @return
     */
    public static CodeSendBox newInstance(String type) {
        switch (type) {
            case "remote":
                return new RemoteCodeSendBox();
            case "thirdParty":
                return new ThirdPartyCodeSendBox();
            case "example":
            default:
                return new ExampleCodeSendBox();
        }
    }

}
