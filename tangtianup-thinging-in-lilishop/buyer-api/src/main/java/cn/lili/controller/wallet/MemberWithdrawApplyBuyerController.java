package cn.lili.controller.wallet;


import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.wallet.entity.dos.MemberWithdrawApply;
import cn.lili.modules.wallet.entity.vo.MemberWithdrawApplyQueryVO;
import cn.lili.modules.wallet.service.MemberWithdrawApplyService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 买家端,余额提现记录接口
 *
 * @author pikachu
 * @since 2020/11/16 10:07 下午
 */
@RestController
@Api(tags = "买家端,余额提现记录接口")
@RequestMapping("/buyer/member/withdrawApply")
public class MemberWithdrawApplyBuyerController {
    @Autowired
    private MemberWithdrawApplyService memberWithdrawApplyService;


    @ApiOperation(value = "分页获取提现记录")
    @GetMapping
    public ResultMessage<IPage<MemberWithdrawApply>> getByPage(PageVO page, MemberWithdrawApplyQueryVO memberWithdrawApplyQueryVO) {
        memberWithdrawApplyQueryVO.setMemberId(UserContext.getCurrentUser().getId());
        //构建查询 返回数据
        IPage<MemberWithdrawApply> memberWithdrawApplyPage = memberWithdrawApplyService.getMemberWithdrawPage(page, memberWithdrawApplyQueryVO);
        return ResultUtil.data(memberWithdrawApplyPage);
    }

}
