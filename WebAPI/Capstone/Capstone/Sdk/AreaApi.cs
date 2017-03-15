using AutoMapper.QueryableExtensions;
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
        public IEnumerable<AreaViewModel> GetAreaByCarParkId(int carParkId)
        {
            return this.BaseService.GetAreaByCarParkId(carParkId)
                .ProjectTo<AreaViewModel>(this.AutoMapperConfig)
                .ToList();
        }

        public void UpdateName(AreaUpdateViewModel model)
        {
            var entity = this.BaseService.Get(model.Id);
            entity.Name = model.Name;
            this.BaseService.Update(entity);
        }

        public bool UpdateStatus(AreaUpdateViewModel model)
        {
            var entity = this.BaseService.Get(model.Id);
            var parkingLotApi = new ParkingLotApi();
            if (entity.EmptyAmount < entity.ParkingLots.Count())
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
    }
}