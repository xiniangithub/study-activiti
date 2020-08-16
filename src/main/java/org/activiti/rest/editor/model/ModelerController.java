package org.activiti.rest.editor.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/modeler")
public class ModelerController {

	/**
	 * 创建模型
	 */
	@SuppressWarnings("deprecation")
    @RequestMapping(value = "/toIndex", method=RequestMethod.GET)
	public void toIndex(HttpServletRequest request, HttpServletResponse response) {
        try {
        	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        	 
        	RepositoryService repositoryService = processEngine.getRepositoryService();
        	 
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, "lutiannan");
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            String description = "lutiannan---";
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName("lutiannan");
            modelData.setKey("12313123");

            //保存模型
            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            response.sendRedirect(request.getContextPath() + "/modeler/modeler.html?modelId=" + modelData.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
