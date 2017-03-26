using AutoMapper.QueryableExtensions;
using Capstone.Models;
using Capstone.Models.Entities.Services;
using Capstone.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Capstone.Sdk
{
    public partial class AreaApi
    {
        public IEnumerable<AreaCustomViewModel> GetAreaByCarParkId(int carParkId)
        {
            return this.BaseService.GetAreaByCarParkId(carParkId)
                .ProjectTo<AreaCustomViewModel>(this.AutoMapperConfig)
                .ToList();
        }

        public AreaCustomViewModel GetAreaWithEmptyNumber(int areaId)
        {
            var entity = this.BaseService.GetAreaWithEmptyNumber(areaId);
            AreaCustomViewModel model = new AreaCustomViewModel
            {
                Active = entity.Active,
                Address = entity.Address,
                CarParkId = entity.CarParkId,
                EmptyAmount = entity.EmptyAmount,
                Id = entity.Id,
                Name = entity.Name,
                Status = entity.Status,
                UpdateAvailable = entity.Active,
            };
            return model;
        }

        public void UpdateName(AreaUpdateViewModel model)
        {
            var entity = this.BaseService.Get(model.Id);
            if (entity != null)
            {
                entity.Name = model.Name;
                this.BaseService.Update(entity);
            }
        }

        public bool UpdateStatus(AreaUpdateViewModel model)
        {
            var entity = this.BaseService.Get(model.Id);
            if (entity != null)
            {
                var parkingLotApi = new ParkingLotApi();
                if (entity.ParkingLots.Count(a => a.Status == (int)ParkingLotStatus.Active || a.Status == (int)ParkingLotStatus.Deactive) < entity.ParkingLots.Count())
                {
                    return false;
                }
                else
                {
                    entity.Status = model.Status;
                    parkingLotApi.UpdateStatus(entity.ParkingLots, model.Status);
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }
}