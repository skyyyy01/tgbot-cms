package com.all.powerful.bot.controller;

import com.all.powerful.bot.domain.UserNotify;
import com.all.powerful.bot.service.IUserNotifyService;
import com.all.powerful.common.annotation.Log;
import com.all.powerful.common.config.AllPowerfulConfig;
import com.all.powerful.common.core.controller.BaseController;
import com.all.powerful.common.core.domain.AjaxResult;
import com.all.powerful.common.core.page.TableDataInfo;
import com.all.powerful.common.enums.BusinessType;
import com.all.powerful.common.utils.StringUtils;
import com.all.powerful.common.utils.file.FileUploadUtils;
import com.all.powerful.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * 通知管理Controller
 *
 * @author all powerful
 * @date 2023-11-27
 */
@Controller
@RequestMapping("/bot/notify")
public class UserNotifyController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(UserNotifyController.class);
    private String prefix = "bot/notify";

    @Autowired
    private IUserNotifyService userNotifyService;

    @RequiresPermissions("bot:notify:view")
    @GetMapping()
    public String notifyUser() {
        return prefix + "/notify";
    }

    /**
     * 查询通知管理列表
     */
    @RequiresPermissions("bot:notify:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(UserNotify userNotify) {
        startPage();
        List<UserNotify> list = userNotifyService.selectUserNotifyList(userNotify);
        return getDataTable(list);
    }

    /**
     * 导出通知管理列表
     */
    @RequiresPermissions("bot:notify:export")
    @Log(title = "通知管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(UserNotify userNotify) {
        List<UserNotify> list = userNotifyService.selectUserNotifyList(userNotify);
        ExcelUtil<UserNotify> util = new ExcelUtil<UserNotify>(UserNotify.class);
        return util.exportExcel(list, "通知管理数据");
    }

    /**
     * 新增通知管理
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存通知管理
     */
    @RequiresPermissions("bot:notify:add")
    @Log(title = "通知管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MultipartFile imageFile, UserNotify userNotify) {
        if (StringUtils.isBlank(userNotify.getText())) {
            return AjaxResult.error("通知内容不能为空");
        }
        if (StringUtils.isBlank(userNotify.getNotifyType())){
            return AjaxResult.error("通知类型不能为空");
        }
        if (userNotify.getNotifyType().equals("1") && StringUtils.isBlank(userNotify.getUserId())){
            return AjaxResult.error("用户ID不能为空");
        }
        File image = null;
        try {
            if (imageFile != null) {
                image = convert(imageFile);
                // 上传文件路径
                String filePath = AllPowerfulConfig.getUploadPath();
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, imageFile);
                userNotify.setImage(fileName);
            }
        } catch (IOException e) {
            log.error("通知图片处理失败", e);
        }
        int row = userNotifyService.insertUserNotify(userNotify, image);

        return toAjax(row);
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file);
             InputStream is = multipartFile.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        return file;
    }

    /**
     * 修改通知管理
     */
    @RequiresPermissions("bot:notify:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        UserNotify userNotify = userNotifyService.selectUserNotifyById(id);
        mmap.put("userNotify", userNotify);
        return prefix + "/edit";
    }

    /**
     * 修改保存通知管理
     */
    @RequiresPermissions("bot:notify:edit")
    @Log(title = "通知管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(UserNotify userNotify) {
        return toAjax(userNotifyService.updateUserNotify(userNotify));
    }

    /**
     * 删除通知管理
     */
    @RequiresPermissions("bot:notify:remove")
    @Log(title = "通知管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(userNotifyService.deleteUserNotifyByIds(ids));
    }
}
