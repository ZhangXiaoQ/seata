package com.itzqy.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itzqy.order.feign.StockService;
import com.itzqy.order.mapper.OrderDetailMapper;
import com.itzqy.order.mapper.OrderInfoMapper;
import com.itzqy.order.service.OrderInfoService;
import com.itzyq.model.dto.OrderInfoDTO;
import com.itzyq.model.entity.OrderDetailEntity;
import com.itzyq.model.entity.OrderInfoEntity;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyq
 * @since 2021-02-02
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfoEntity> implements OrderInfoService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private StockService stockService;

    @Override

    @GlobalTransactional
    public void createOrderInfo(OrderInfoDTO orderInfoDTO) {
        OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
        orderInfoEntity.setOrderNum(orderInfoDTO.getOrderNum());
        orderInfoMapper.insert(orderInfoEntity);

        List<OrderDetailEntity> stockList = orderInfoDTO.getStockList();
        stockList.stream().forEach(item->{
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity()
                    .setOrderId(orderInfoEntity.getId())
                    .setStockId(item.getStockId());
            orderDetailMapper.insert(orderDetailEntity);
        });
        stockService.updateStockInfo(orderInfoDTO);
    }
}
